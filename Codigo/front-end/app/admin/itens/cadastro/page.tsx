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
    <div className='bg-white rounded-xl w-full h-full p-5'>
      <h3 className='text-xl font-semibold'>Cadastro</h3>
      <form onSubmit={handleSubmit}>
        <Label htmlFor='nome'>Nome</Label>
        <Input placeholder='Nome do item' name='nome' value={nome} type="text"
          onChange={e => setNome(e.target.value)} required />
        <Label htmlFor='imagem'>Imagem</Label>
        <Input name='imagem' type='file' accept="image/*" onChange={handleImageChange} />
        <Label htmlFor="quantidade">Quantidade</Label>
        <Input placeholder='0' type='number' name='quantidade' value={quantidade}
          onChange={e => setQuantidade(e.target.value)} required />
        <Label htmlFor="valor_unitario">Valor unitário</Label>
        <Input placeholder='R$ 000,00' name='valor_unitario' value={valor_unitario}
          onChange={e => setValor(e.target.value)} required />
        <div className='flex flex-col space-y-3'>
          <Label htmlFor='emprestavel'>Emprestavel?</Label>
          <Switch name='emprestavel' checked={emprestavel}
            onCheckedChange={setIsToggled} />
        </div>
        <Label htmlFor='tipo_item'>Tipo do item</Label>
        <Select name='tipo_item' value={tipo_item} onValueChange={setSelectedOption}>
          <SelectTrigger>
            <SelectValue placeholder='Selecione o tipo do item' />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value='VIDRARIA'>Vidraria</SelectItem>
            <SelectItem value='EQUIPAMENTO'>Equipamento</SelectItem>
          </SelectContent>
        </Select>
        <Button type='submit'>Confirmar</Button>
      </form>
    </div>
  );
}

export default CadastroItem;