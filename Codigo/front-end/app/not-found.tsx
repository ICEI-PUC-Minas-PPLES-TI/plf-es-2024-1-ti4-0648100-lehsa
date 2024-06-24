import Link from 'next/link'
 
export default function NotFound() {
  return (
    <div className='p-10'>
      <h2 className='text-3xl'>Página não encontrada</h2>
      <Link href="/">Voltar a página principal</Link>
    </div>
  )
}