'use client'

import FilterSelect from '@/components/FilterSelect';
import ItensCard from '@/components/ItemCard'
import SearchBar from '@/components/SearchBar';
import React, { useState } from 'react'

const UserPage = () => {

  const [searchTerm, setSearchTerm] = useState('');

  return (
    <div className='max-w-7xl m-auto space-y-5'>
      <div className='flex space-x-5'>
        <div className='w-1/2 bg-white h-96 rounded-2xl p-5'>
          <h2 className='font-semibold text-xl'>Seus Agendamentos</h2>
        </div>
        <div className='w-1/2 bg-white h-96 rounded-2xl p-5'>
          <h2 className='font-semibold text-xl'>Dados da sua conta</h2>
          
        </div>
      </div>
      <div className='w-full bg-white   rounded-2xl p-5'>
        <h2 className='font-semibold text-xl mb-6'>Itens disponíveis para agendamento</h2>
        <div className='flex flex-1 justify-between my-5'>
          <SearchBar onChange={(e) => setSearchTerm(e.target.value)} />
          <div className='flex justify-items-end space-x-5'>
            <FilterSelect />

          </div>
        </div>
        <ItensCard searchTerm={searchTerm} />
      </div>
    </div>
  )
}

export default UserPage