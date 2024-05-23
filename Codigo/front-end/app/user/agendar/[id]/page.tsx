"use client";

import React, { useEffect, useState, ChangeEvent } from "react";
import Link from "next/link";
import Cookie from "js-cookie";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import SearchBar from "@/components/SearchBar";
import { useRouter } from "next/navigation";
import { Calendar } from "@/components/ui/calendar";
import { Button } from "@/components/ui/button";
import { CalendarIcon, X, Plus, ArrowLeftToLine } from "lucide-react";
import { Textarea } from "@/components/ui/textarea";
import { format } from "date-fns";
import { cn } from "@/lib/utils";
import { Input } from "@/components/ui/input";
import { jwtDecode } from "jwt-decode";
import { Label } from "@/components/ui/label";
import TopMenu from "@/components/topMenu";
import SingleItemCard from "@/components/SingleItemCard";

interface EmailObject {
  email: string;
}

interface ItemDisplay {
  id: string;
  nome: string;
  qtde: number;
  tipo_item: string;
}

interface ItemSend {
  id: string;
  quantidade_transacao: number;
}

const Agendar = ({ params }: { params: { id: string } }) => {
  const router = useRouter();

  const [data, setData] = React.useState<Date>();
  const [timeInicio, setTimeInicio] = useState("");
  const [timeFim, setTimeFim] = useState("");
  const [obs, setObs] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [email, setEmail] = useState<string>("");
  const [solicitantes, setSolicitantes] = useState<string[]>([]);
  const [emailProfessor, setEmailProfessor] = useState("");
  const [itensDisplay, setItensDisplay] = useState<ItemDisplay[]>([]);
  const [allItens, setAllItens] = useState<ItemDisplay[]>([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [open, setOpen] = React.useState(false);
  const token = Cookie.get("token");
  let decoded = "";
  if (token) {
    decoded = jwtDecode(token);
  }

  useEffect(() => {
    getItem();
    getAllItens();
  }, []);

  const getItem = async () => {
    try {
      const response = await fetch(`http://localhost:8080/item/${params.id}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error("Failed to fetch item");
      }
      const data = await response.json();
      const newItem = {
        id: data.id,
        nome: data.nome,
        qtde: 1,
        tipo_item: data.tipo_item,
      };
      setItensDisplay([...itensDisplay, newItem]);
    } catch (error) {
      console.error("Failed to fetch item:", error);
    }
  };

  const getAllItens = async () => {
    try {
      const response = await fetch(`http://localhost:8080/item`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error("Failed to fetch item");
      }
      const data = await response.json();
      setAllItens(data);
    } catch (error) {
      console.error("Failed to fetch item:", error);
    }
  };

  const filteredItems = allItens.filter((item) =>
    item.nome.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleNewItem = (id: string, nome: string, qtde: number, tipo_item: string) => {
    const newItem: ItemDisplay = {id: id, nome: nome, qtde: qtde, tipo_item: tipo_item}
    setItensDisplay([...itensDisplay, newItem]);
    setOpen(false)
  }

  const handleChangeEmail = (e: ChangeEvent<HTMLInputElement>) => {
    setEmail(e.target.value);
  };

  const handleAddEmail = () => {
    if (email && !solicitantes.includes(email)) {
      setSolicitantes([...solicitantes, email]);
      setEmail("");
    }
  };

  const handleRemoveEmail = (mail: string) => {
    setSolicitantes((prevEmails) => prevEmails.filter((item) => item !== mail));
  };

  const handleChangeInicio = (event: React.ChangeEvent<HTMLInputElement>) => {
    const value = event.target.value;
    let cleanValue = value.replace(/[^0-9]/g, "");
    cleanValue = cleanValue.slice(0, 4);
    if (cleanValue.length > 2) {
      cleanValue = cleanValue.slice(0, 2) + ":" + cleanValue.slice(2);
    }
    setTimeInicio(cleanValue);
  };

  const handleChangeFim = (event: React.ChangeEvent<HTMLInputElement>) => {
    const value = event.target.value;
    let cleanValue = value.replace(/[^0-9]/g, "");
    cleanValue = cleanValue.slice(0, 4);
    if (cleanValue.length > 2) {
      cleanValue = cleanValue.slice(0, 2) + ":" + cleanValue.slice(2);
    }
    setTimeFim(cleanValue);
  };

  const handleQuantityChange = (id: string, newQuantity: number) => {
    if (isNaN(newQuantity) || newQuantity < 0) return; // Validar que a quantidade é um número positivo
    setItensDisplay((prevItemList) =>
      prevItemList.map((item) =>
        item.id === id ? { ...item, qtde: newQuantity } : item
      )
    );
  };

  const handleRemoveItem = (id: string) => {
    setItensDisplay((prevItemList) =>
      prevItemList.filter((item) => item.id !== id)
    );
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    const professor = { email: emailProfessor };
    const simplifiedList: ItemSend[] = itensDisplay.map(item => ({
      id: item.id,
      quantidade_transacao: item.qtde
    }));
    const userMailObj: EmailObject = { email: `${decoded.sub}` };
    const emailObjects: EmailObject[] = solicitantes.map((email) => ({
      email,
    }));
    emailObjects.push(userMailObj);
    const dateFormat = format(data ? data : "", "dd/MM/yyyy");
    const dataHoraInicio = `${dateFormat} ${timeInicio}:00`;
    const dataHoraFim = `${dateFormat} ${timeFim}:00`;
    // const itens = [{ id: params.id, quantidade_transacao: 1 }];
    const observacaoSolicitacao = obs;
    try {
      const response = await fetch("http://localhost:8080/agendamento", {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          dataHoraInicio,
          dataHoraFim,
          itens: simplifiedList,
          solicitantes: emailObjects,
          observacaoSolicitacao,
          professor: professor,
        }),
      });
      if (!response.ok) {
        return setErrorMessage("Erro ao enviar formulário");
      }
      router.push("/user");
    } catch (error: any) {
      console.error("Erro ao enviar o formulário:", error.message);
    }
  };

  return (
    <div>
      <div className="flex items-center gap-4">
        <Link href="/user">
          <ArrowLeftToLine className="bg-white p-1 size-8 text-primary rounded-full" />
        </Link>
        <TopMenu title="Solicitar agendamento" />
      </div>
      <div className="pt-20">
        <form
          onSubmit={handleSubmit}
          className="bg-white p-8 rounded-md w-[600px] m-auto"
        >
          <div className="space-y-4">
            <Popover>
              <PopoverTrigger asChild>
                <div className="flex flex-col">
                  <Label htmlFor="calendario">Data desejada</Label>
                  <Button
                    name="calendario"
                    variant={"outline"}
                    className={cn(
                      "w-[280px] justify-start text-left font-normal",
                      !data && "text-muted-foreground"
                    )}
                  >
                    <CalendarIcon className="mr-2 h-4 w-4" />
                    {data ? (
                      format(data, "dd/MM/yyyy")
                    ) : (
                      <span>Selecione a data</span>
                    )}
                  </Button>
                </div>
              </PopoverTrigger>
              <PopoverContent className="w-auto p-0">
                <Calendar
                  mode="single"
                  selected={data}
                  onSelect={setData}
                  initialFocus
                />
              </PopoverContent>
            </Popover>
            <div className="flex space-x-4">
              <div>
                <Label htmlFor="horaInicio">Horário de Início</Label>
                <Input
                  placeholder="00:00"
                  value={timeInicio}
                  onChange={handleChangeInicio}
                  name="horaInicio"
                />
              </div>
              <div>
                <Label htmlFor="horaFim">Horário de Término</Label>
                <Input
                  placeholder="00:00"
                  value={timeFim}
                  onChange={handleChangeFim}
                  name="horaFim"
                />
              </div>
            </div>
            <div>
              <Label htmlFor="emailProf">Professor responsável</Label>
              <Input
                name="emailProf"
                placeholder="Email do professor"
                value={emailProfessor}
                onChange={(e) => setEmailProfessor(e.target.value)}
              />
            </div>
            <div>
              <div className="flex flex-col mb-3">
                <Label htmlFor="emails" className="mb-1">
                  Outros participantes:
                </Label>
                <div className="flex items-center gap-3">
                  <Input
                    placeholder="Digite um email..."
                    value={email}
                    onChange={handleChangeEmail}
                  />
                  <Plus
                    onClick={handleAddEmail}
                    className="hover:cursor-pointer"
                  />
                </div>
              </div>
              {solicitantes.map((mail) => (
                <div className="flex space-x-2 items-center">
                  <span className="text-[14px]">{mail}</span>
                  <X
                    className="cursor-pointer w-5"
                    onClick={() => handleRemoveEmail(mail)}
                  />
                </div>
              ))}
            </div>
            <div>
              <div className="space-x-4">
                <Label htmlFor="itens">Itens no agendamento</Label>
                <Dialog open={open} onOpenChange={setOpen}>
                  <DialogTrigger className="text-sm p-0 text-primary">
                    Adicionar outros
                  </DialogTrigger>
                  <DialogContent>
                    <DialogHeader>
                      <DialogTitle>Selecione um Item</DialogTitle>
                    </DialogHeader>
                    <SearchBar
                      onChange={(e) => setSearchTerm(e.target.value)}
                    />
                    <div className="flex">
                      {filteredItems.map((item) => (
                        <div onClick={() => handleNewItem(item.id, item.nome, item.qtde, item.tipo_item)} className="cursor-pointer">
                          <SingleItemCard
                            id={item.id}
                            nome={item.nome}
                            tipo_item={item.tipo_item}
                            quantidade={item.qtde}
                          />
                        </div>
                      ))}
                    </div>
                  </DialogContent>
                </Dialog>
              </div>
              {itensDisplay.map((item) => (
                <div key={item.id} className="flex gap-5 my-2 items-center">
                  <p className="text-sm">{item.nome}</p>
                  <div className="flex items-center gap-2">
                    <p className="text-sm">Qtde:</p>
                    <Input
                      type="number"
                      value={item.qtde}
                      onChange={(e: ChangeEvent<HTMLInputElement>) =>
                        handleQuantityChange(
                          item.id,
                          parseInt(e.target.value, 10)
                        )
                      }
                      className="w-24"
                    />
                    <X
                      className="cursor-pointer w-5"
                      onClick={() => handleRemoveItem(item.id)}
                    />
                  </div>
                  <hr />
                </div>
              ))}
            </div>
            <div>
              <Label htmlFor="observacoes">Observações</Label>
              <Textarea
                placeholder="Preencha com informações que julgar necessário"
                value={obs}
                onChange={(e) => setObs(e.target.value)}
                name="observacoes"
              />
            </div>
            <Button type="submit">Confirmar agendamento</Button>

            {errorMessage && (
              <p className="font-semibold text-red-600 mt-3 text-center">
                {errorMessage}
              </p>
            )}
          </div>
        </form>
      </div>
    </div>
  );
};

export default Agendar;
