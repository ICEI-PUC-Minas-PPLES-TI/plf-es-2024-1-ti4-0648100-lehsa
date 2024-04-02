import Link from "next/link";
import Image from "next/image";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";

export default function Home() {
  return (
    <main className="flex h-screen">
      <div className="bg-white w-1/3 min-w-80 p-10 space-y-14">
        <Image src='/logo.jpg' width={150} height={0} alt='logo' ></Image>
        <div>
          <h2 className="font-bold text-3xl">Log in.</h2>
          <p className="lg:max-w-[80%] mb-6">Lorem ipsum dolor sit amet, consectetur adipiscing elit </p>
          <Label htmlFor="email">Email</Label>
          <Input placeholder="exemplo@email.com" id="email" type="email"/>
          <Label htmlFor="password">Senha</Label>
          <Input placeholder="mínimo de 8 caracteres" id="password" type="password"/>
          <Link href='/admin'><Button className="w-full mt-9">Login</Button></Link>
          <p className="text-center mt-12">Não possui uma conta? <Link href='/user/cadastrar'><button className="text-primary font-semibold cursor-pointer" >Cadastre-se</button></Link></p>
        </div>
      </div>
      
      <div className="m-auto space-y-6">
        <p className="text-center text-lg">Lorem ipsum dolor</p>
        <h2 className="text-center text-primary font-bold text-5xl">Lorem ipsum dolor</h2>
        <Image src='/login-image.png' width={250} height={0} alt="imagem" className="m-auto"/>
      </div>

    </main>
  );
}
