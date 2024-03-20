import { Button } from '@/components/ui/button'
import Link from 'next/link'
import React from 'react'

const Equipamentos = () => {
  return (
    <div>      
      <Link href='/admin/itens/cadastro'><Button>+ Novo Item</Button></Link>
    </div>
  )
}

export default Equipamentos