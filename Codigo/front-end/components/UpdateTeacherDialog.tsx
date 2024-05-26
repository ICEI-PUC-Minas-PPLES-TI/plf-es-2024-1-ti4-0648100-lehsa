"use client";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useEffect, useState } from "react";
import Cookie from "js-cookie";

interface UpdateTeacherDialogProps {
  children: React.ReactNode;
  id: string;
}

export const UpdateTeacherDialog: React.FC<UpdateTeacherDialogProps> = ({ children, id }) => {

  const [matricula, setMatricula] = useState("");
  const [campus, setCampus] = useState("");
  const [lotacao, setLotacao] = useState("");
  const [areaAtuacao, setAreaAtuacao] = useState("");
  const [laboratorio, setLaboratorio] = useState("");
  const [nome, setNome] = useState("");
  const [email, setEmail] = useState("");
  const [imagem, setImagem] = useState<File | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      const token = Cookie.get("token");
      const response = await fetch(`http://localhost:8080/professor/${id}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.ok) {
        const data = await response.json();
        setMatricula(data.matricula);
        setCampus(data.campus);
        setLotacao(data.lotacao);
        setAreaAtuacao(data.area_atuacao);
        setLaboratorio(data.laboratorio);
        setNome(data.nome);
        setEmail(data.email);
        setImagem(data.imagem);
      }
    };

    fetchData();
  }, [id]);

  const handleImageChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files) {
      setImagem(event.target.files[0]);
    }
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const formData = new FormData();
    formData.append(
      "professor",
      new Blob(
        [
          JSON.stringify({
            matricula,
            campus,
            lotacao,
            areaAtuacao,
            laboratorio,
            nome,
            email,
          }),
        ],
        {
          type: "application/json",
        },
      ),
    );
    if (imagem) {
      formData.append("imagem", imagem);
    }

    const token = Cookie.get("token");
    try {
      const response = await fetch(`http://localhost:8080/professor/${id}`, {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${token}`,
        },
        body: formData,
      });
      if (!response.ok) {
        throw new Error("Falha ao enviar o formulário");
      }
      const toaster = document.createElement("div");
      toaster.classList.add(
        "bg-green-500",
        "text-white",
        "p-4",
        "rounded",
        "fixed",
        "bottom-4",
        "left-1/2",
        "transform",
        "-translate-x-1/2",
      );
      toaster.textContent = "Professor atualizado!";
      document.body.appendChild(toaster);
      setTimeout(() => {
        document.body.removeChild(toaster);
      }, 3000);
    } catch (error) {
      console.error("Erro ao enviar o formulário:", error);
    }
  };

  return (
    <Dialog>
      <DialogTrigger asChild>{children}</DialogTrigger>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>Editar professor</DialogTitle>
          <DialogDescription>
            Atualize os dados do professor aqui. Clique em salvar para finalizar.
          </DialogDescription>
        </DialogHeader>
        <form onSubmit={handleSubmit}>
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
                />
              </Label>
            </div>
            <div>
              <Label htmlFor="email">
                E-mail
                <Input
                  placeholder="exemplo@email.com"
                  type="email"
                  name="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                />
              </Label>
            </div>
            <div>
              <Label htmlFor="imagem">
                Imagem
                <Input
                  name="imagem"
                  type="file"
                  accept="image/*"
                  onChange={handleImageChange}
                />
              </Label>
            </div>
          </div>
          <DialogFooter>
            <Button type="submit">Salvar</Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
};
