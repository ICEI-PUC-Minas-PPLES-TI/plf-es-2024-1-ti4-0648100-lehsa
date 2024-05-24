import TeacherCard from "@/components/TeacherCard";
import TopMenu from "@/components/topMenu";
import { Button } from "@/components/ui/button";
import Link from "next/link";

const Professores = () => {
  
    return (
      <div className="">
        <TopMenu title="Professores" />
        <div className='mt-10'>
        <Link href='/admin/emprestimo/cadastro'><Button>+ Novo Professor</Button></Link>
        </div>
        <TeacherCard/>
      </div>
    );
  };
  
  export default Professores;