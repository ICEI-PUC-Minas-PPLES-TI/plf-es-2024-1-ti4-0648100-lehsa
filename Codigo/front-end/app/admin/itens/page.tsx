import ItensCard from '@/components/ItemCard'
import { Button } from '@/components/ui/button'
import Link from 'next/link'
import React from 'react'

const Equipamentos = () => {
  return (
    <div>
      <Link href='/admin/itens/cadastro' className='pl-60'><Button>+ Novo Item</Button></Link>
      <ItensCard />
    </div>
  )
}

export default Equipamentos