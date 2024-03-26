import React from 'react'
import Image from 'next/image'

type Props = {
    title: string
}

const TopMenu = ( {title}: Props ) => {
  return (
    <div className='flex w-full justify-between items-center'>
        <h1 className='text-black text-3xl font-semibold pl-60'>{title}</h1>
        <div className='flex gap-5'>
            <Image src='/icons/notification.svg' width={46} height={0} alt='notification'></Image>
            <Image src='/icons/setting.svg' width={46} height={0} alt='setting'></Image>
            <div className='text-center'>
                <p className='text-sm'><span className='font-semibold text-base'>Ruth</span><br/>admin</p>
            </div>
            <Image src='/icons/profile.svg' width={46} height={0} alt='profile'></Image>
        </div>
    </div>
  )
}

export default TopMenu