'use client'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Switch } from '@/components/ui/switch'
import React, { FormEvent, useRef, useState } from 'react'
import { useRouter } from "next/navigation";
import Cookie from 'js-cookie'

// Importações permanecem as mesmas

const CadastroItem = () => {
  const router = useRouter();

  const formRef = useRef<HTMLFormElement>(null);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    const formData = new FormData(event.currentTarget);
    const nome = formData.get('nome') as string;
    const quantidadeRaw = formData.get('quantidade');
    const valorUnitarioRaw = formData.get('valor_unitario');
    const tipoItem = formData.get('tipo_item') as string;
    const emprestavelRaw = formData.get('emprestavel');

    const quantidade = quantidadeRaw ? parseInt(quantidadeRaw.toString(), 10) : 0; 
    const valorUnitario = valorUnitarioRaw ? parseFloat(valorUnitarioRaw.toString()) : 0.0;
    const emprestavel = emprestavelRaw === 'true';


    const token = Cookie.get("token");

    const item = {
      nome, quantidade, valorUnitario, tipoItem, emprestavel
    }

    const options = {
      method: 'POST',
      headers: {
        'Content-Type': 'multipart/form-data; boundary=---011000010111000001101001',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify(item),
    };

    try {
      const response = await fetch('http://localhost:8080/item', options);

      if (response.ok) {
        router.push('/admin/itens');
      } else {
        // Tratar a resposta não-OK aqui
        console.error('Falha na resposta:', await response.text());
      }
    } catch (error) {
      console.error('Erro na requisição:', error);
    }
  }

  // O JSX permanece inalterado, exceto pela correção no nome do input da imagem
  return (
    <div className='bg-white rounded-xl w-full h-full p-5'>
      <h3 className='text-xl font-semibold'>Cadastro</h3>
      <form onSubmit={handleSubmit} ref={formRef}>
        <Label htmlFor='nome'>Nome</Label>
        <Input placeholder='Nome do item' name='nome' required />
        {/* <Label htmlFor='imagem'>Imagem</Label>
        <Input name='imagem' type='file' accept="image/*" /> */}
        <Label htmlFor="quantidade">Quantidade</Label>
        <Input placeholder='0' type='number' name='quantidade' required />
        <Label htmlFor="valor_unitario">Valor unitário</Label>
        <Input placeholder='R$ 000,00' name='valor_unitario' required />
        <div className='flex flex-col space-y-3'>
          <Label htmlFor='emprestavel'>Emprestavel?</Label>
          <Switch name='emprestavel' />
        </div>
        <Label htmlFor='tipo_item'>Tipo do item</Label>
        <Select name='tipo_item'>
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