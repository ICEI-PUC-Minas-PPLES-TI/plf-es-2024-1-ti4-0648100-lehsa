import React, { ReactNode } from 'react'

interface Props {
    children: ReactNode
}

const adminLayout = ({ children }: Props) => {
  return (
    <div className='flex'>
        <div className='p-6 grow h-screen overflow-y-auto'>{ children }</div>
    </div>
  )
}
    
export default adminLayout