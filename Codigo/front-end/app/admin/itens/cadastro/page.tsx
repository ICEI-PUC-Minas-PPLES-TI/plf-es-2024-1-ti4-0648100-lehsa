'use client'

import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Switch } from '@/components/ui/switch'
import React, { useState } from 'react'
import { useRouter } from "next/navigation";
import Cookie from 'js-cookie'

const CadastroItem = () => {
  const router = useRouter();

  const [nome, setNome] = useState('');
  const [imagem, setImagem] = useState<File | null>(null);
  const [quantidade, setQuantidade] = useState('');
  const [valor_unitario, setValor] = useState('');
  const [emprestavel, setIsToggled] = useState(false);
  const [tipo_item, setSelectedOption] = useState<string>('');

  const handleImageChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files) {
      setImagem(event.target.files[0]);
    }
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const formData = new FormData();
    formData.append('item', new Blob([JSON.stringify({ nome, quantidade, valor_unitario, emprestavel, tipo_item })], {
      type: "application/json"
    }));
    if (imagem) {
      formData.append('imagem', imagem);
    }

    const token = Cookie.get("token");
    try {
      const response = await fetch('http://localhost:8080/item', {
        method: 'POST',
        headers: {
          "Authorization": `Bearer ${token}`
        },
        body: formData
      });
      if (!response.ok) {
        throw new Error('Falha ao enviar o formulário');
      }
      router.push('/admin/itens')
    } catch (error: any) {
      console.error('Erro ao enviar o formulário:', error.message);
    }
  };

  return (
    <div className='bg-white rounded-xl w-fit h-fit flex flex-col m-auto'>
      <div className='bg-primary h-14 content-center p-6 rounded-t-xl'>
        <h3 className='text-xl font-semibold flex-none text-white'>Cadastro</h3>
      </div>
      <form onSubmit={handleSubmit} className='grow flex flex-col justify-between p-6'>
        <div>
          <div className='flex space-x-6'>
            <div className='space-y-4'>
              <div>
                <Label htmlFor='nome'>
                  Nome
                  <Input placeholder='Nome do item' name='nome' value={nome} type="text"
                    onChange={e => setNome(e.target.value)} required />
                </Label>
              </div>
              <div className='flex space-x-6'>
                <div>
                  <Label htmlFor="quantidade">
                    Quantidade
                    <Input placeholder='0' type='number' name='quantidade' value={quantidade}
                      onChange={e => setQuantidade(e.target.value)} required />
                  </Label>
                </div>
                <div>
                  <Label htmlFor="valor_unitario">
                    Valor unitário
                    <Input placeholder='R$ 000,00' type='number' name='valor_unitario' value={valor_unitario}
                      onChange={e => setValor(e.target.value)} required />
                  </Label>
                </div>
              </div>
            </div>
            <div>
              <Label htmlFor='imagem'>
                Imagem
                <Input name='imagem' type='file' accept="image/*" onChange={handleImageChange} />
              </Label>
            </div>
          </div>

          <div className='flex space-x-6 space-y-4'>
            <div className='pt-5'>
              <Label htmlFor='emprestavel' className='flex flex-col gap-3'>
                Emprestavel?
                <Switch name='emprestavel' checked={emprestavel}
                  onCheckedChange={setIsToggled} />
              </Label>
            </div>

            <div>
              <Label htmlFor='tipo_item'>
                Tipo do item
                <Select name='tipo_item' value={tipo_item} onValueChange={setSelectedOption}>
                  <SelectTrigger>
                    <SelectValue placeholder='Selecione o tipo do item' />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value='VIDRARIA'>Vidraria</SelectItem>
                    <SelectItem value='EQUIPAMENTO'>Equipamento</SelectItem>
                  </SelectContent>
                </Select>
              </Label>
            </div>
          </div>
        </div>

        <Button className='self-end' type='submit'>Confirmar</Button>
      </form>
    </div>
  );
}

export default CadastroItem;