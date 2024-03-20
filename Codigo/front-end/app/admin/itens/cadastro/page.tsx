'use client'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Switch } from '@/components/ui/switch'
import React, { useState } from 'react'

const CadastroItem = () => {

  return (
    <div className='bg-white rounded-xl w-full h-full p-5'>
      <h3 className='text-xl font-semibold'>Cadastro</h3>
      <Label htmlFor='nome'>Nome</Label>
      <Input placeholder='nome do item' id='nome' />
      <Label htmlFor="quantidade">Quantidade</Label>
      <Input placeholder='0' id='quantidade' />
      <Label htmlFor="valor">Valor</Label>
      <Input placeholder='R$ 000,00' id='valor' />
      <div className='flex flex-col space-y-3'>
        <Label htmlFor='emprestavel'>Emprestavel?</Label>
        <Switch id='emprestavel' />
      </div>
      <Label htmlFor='categoria'>Categoria</Label>
      <Select>
        <SelectTrigger>
          <SelectValue placeholder='Categoria'/>
        </SelectTrigger>
        <SelectContent>
          <SelectItem value='material'>Material</SelectItem>
          <SelectItem value='insumo'>Insumo</SelectItem>
          <SelectItem value='equipamento'>Equipamento</SelectItem>
          <SelectItem value='outro'>Outro</SelectItem>
        </SelectContent>
      </Select>

    </div>
  )
}

export default CadastroItem