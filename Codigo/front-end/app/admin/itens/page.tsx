import ItensCard from '@/components/ItensCard'
import { Button } from '@/components/ui/button'
import Link from 'next/link'
import React from 'react'
import SearchBar from "@/components/SearchBar";
import FilterSelect from "@/components/FilterSelect";

const Equipamentos = () => {
  return (
    <div>
        <div className=' flex flex-1 justify-between my-5'>
            <SearchBar />
            <div className='flex justify-items-end space-x-5'>
                <FilterSelect />
                <Link href='/admin/itens/cadastro'><Button>+ Novo Item</Button></Link>
            </div>
        </div>
      <ItensCard />
    </div>
  )
}

export default Equipamentos