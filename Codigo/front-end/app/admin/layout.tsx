import React, { ReactNode } from 'react'
import NavBar from './NavBar'


interface Props {
    children: ReactNode
}

const adminLayout = ({ children }: Props) => {
  return (
    <div className='flex'>
        <NavBar />
        <div className='p-6'>{ children }</div>
    </div>
  )
}
    
export default adminLayout