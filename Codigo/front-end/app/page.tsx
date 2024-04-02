'use client'
import Image from "next/image";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";

import { FormEvent } from 'react'
import { useRouter } from 'next/navigation'

export default function Home() {

  const router = useRouter()

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()

    const formData = new FormData(event.currentTarget)
    const email = formData.get('email')
    const password = formData.get('password')

    const response = await fetch('http://localhost:8080/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password }),
    })

    if (response.ok) {
      router.push('/dashboard')
    } else {
      window.alert("erro mano");
    }
  }

  return (
    <main className="flex h-screen">
      <div className="bg-white w-1/3 min-w-80 p-10 space-y-14">
        <Image src='/logo.jpg' width={150} height={0} alt='logo' ></Image>
        <div>
          <h2 className="font-bold text-3xl">Log in.</h2>
          <p className="lg:max-w-[80%] mb-6">Lorem ipsum dolor sit amet, consectetur adipiscing elit </p>
          <form onSubmit={handleSubmit}>
            <Label htmlFor="email">Email</Label>
            <Input placeholder="exemplo@email.com" name="email" type="email" />
            <Label htmlFor="password">Senha</Label>
            <Input placeholder="mínimo de 8 caracteres" name="password" type="password" />
            <Button type="submit"  className="w-full mt-9">Login</Button>
          </form>
          <p className="text-center mt-12">Não possui uma conta? <span className="text-primary font-semibold">Cadastre-se</span></p>
        </div>
      </div>

      <div className="m-auto space-y-6">
        <p className="text-center text-lg">Lorem ipsum dolor</p>
        <h2 className="text-center text-primary font-bold text-5xl">Lorem ipsum dolor</h2>
        <Image src='/login-image.png' width={250} height={0} alt="imagem" className="m-auto" />
      </div>

    </main>
  );
}
