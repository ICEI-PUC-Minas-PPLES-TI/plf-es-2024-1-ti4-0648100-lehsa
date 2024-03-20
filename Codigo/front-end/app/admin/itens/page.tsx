import ItensCard from '@/components/ItemCard'
import { Button } from '@/components/ui/button'
import Link from 'next/link'
import React from 'react'

const Equipamentos = () => {
  return (
    <div>      
      <Link href='/admin/itens/cadastro'><Button>+ Novo Item</Button></Link>
      <ItensCard />
    </div>
  )
}

export default Equipamentos