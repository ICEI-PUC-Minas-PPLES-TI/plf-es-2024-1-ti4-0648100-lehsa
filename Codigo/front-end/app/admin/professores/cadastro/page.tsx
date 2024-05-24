"use client";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useRouter } from "next/navigation";
import { useState } from "react";
import Cookie from "js-cookie";
import { Button } from "@/components/ui/button";

const CadastroProfessor = () => {
  const router = useRouter();

  const [matricula, setMatricula] = useState("");
  const [campus, setCampus] = useState("");
  const [lotacao, setLotacao] = useState("");
  const [areaAtuacao, setAreaAtuacao] = useState("");
  const [laboratorio, setLaboratorio] = useState("");
  const [nome, setNome] = useState("");
  const [email, setEmail] = useState("");

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    const token = Cookie.get("token");
    try {
      const response = await fetch("http://localhost:8080/professor", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          matricula,
          campus,
          lotacao,
          areaAtuacao,
          laboratorio,
          nome,
          email,
        }),
      });
      if (!response.ok) {
        throw new Error("Falha ao enviar o formulário");
      }
      router.push("/admin/professores");
    } catch (error) {
      console.error("Erro ao enviar o formulário:", error);
    }
  };

  return (
    <div className="bg-white rounded-xl w-fit h-fit flex flex-col m-auto my-20">
      <div className="bg-primary h-14 content-center p-6 rounded-t-xl">
        <h3 className="text-xl font-semibold flex-none text-white">Cadastro</h3>
      </div>
      <form
        onSubmit={handleSubmit}
        className="grow flex flex-col justify-between p-6"
      >
        <div>
          <div className="grid grid-cols-2 gap-6">
            <div>
              <Label htmlFor="nome">
                Nome
                <Input
                  placeholder="Nome do professor"
                  name="nome"
                  value={nome}
                  type="text"
                  onChange={(e) => setNome(e.target.value)}
                  required
                />
              </Label>
            </div>
            <div>
              <Label htmlFor="matricula">
                Matrícula
                <Input
                  placeholder=""
                  type="text"
                  name="matricula"
                  value={matricula}
                  onChange={(e) => setMatricula(e.target.value)}
                  required
                />
              </Label>
            </div>
            <div>
              <Label htmlFor="campus">
                Campus
                <Input
                  placeholder=""
                  type="text"
                  name="campus"
                  value={campus}
                  onChange={(e) => setCampus(e.target.value)}
                  required
                />
              </Label>
            </div>
            <div>
              <Label htmlFor="lotacao">
                Lotação
                <Input
                  placeholder=""
                  type="text"
                  name="lotacao"
                  value={lotacao}
                  onChange={(e) => setLotacao(e.target.value)}
                  required
                />
              </Label>
            </div>
            <div>
              <Label htmlFor="areaAtuacao">
                Área de atuação
                <Input
                  placeholder=""
                  type="text"
                  name="areaAtuacao"
                  value={areaAtuacao}
                  onChange={(e) => setAreaAtuacao(e.target.value)}
                  required
                />
              </Label>
            </div>
            <div>
              <Label htmlFor="laboratorio">
                Laboratório
                <Input
                  placeholder=""
                  type="text"
                  name="laboratorio"
                  value={laboratorio}
                  onChange={(e) => setLaboratorio(e.target.value)}
                  required
                />
              </Label>
            </div>
            <div className="col-span-2">
              <Label htmlFor="email">
                E-mail
                <Input
                  placeholder="exemplo@email.com"
                  type="text"
                  name="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
              </Label>
            </div>
          </div>
        </div>
        <div className="flex flex-1 self-end mt-4 gap-5">
          <Button
            type="button"
            onClick={() => router.push("/admin/professores")}
          >
            Voltar
          </Button>
          <Button className="bg-green-500 hover:bg-green-600" type="submit">
            Confirmar
          </Button>
        </div>
      </form>
    </div>
  );
};

export default CadastroProfessor;
