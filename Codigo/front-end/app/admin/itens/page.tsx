'use client'
import ItensCard from '@/components/ItemCard'
import { Button } from '@/components/ui/button'
import Link from 'next/link'
import React, { useState } from 'react'
import SearchBar from "@/components/SearchBar";
import FilterSelect from "@/components/FilterSelect";
const Equipamentos = () => {

  const [searchTerm, setSearchTerm] = useState('');

  return (
    <div>
      <div className='flex flex-1 justify-between my-5'>
        <SearchBar onChange={(e) => setSearchTerm(e.target.value)} />
        <div className='flex justify-items-end space-x-5'>
          <Link href='/admin/itens/cadastro'><Button>+ Novo Item</Button></Link>
        </div>
      </div>
      <ItensCard searchTerm={searchTerm} />
    </div>
  )
}

export default Equipamentos