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
import SimplifiedItem from "@/components/SimplifiedItem";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

interface EmailObject {
  email: string;
}

interface ItemDisplay {
  id: string;
  nome: string;
  quantidade: number;
  tipo_item: string;
}

interface ItemSend {
  id: string;
  quantidade_transacao: number;
}

const estados = [
  { nome: "AC", valor: "ACRE" },
  { nome: "AL", valor: "ALAGOAS" },
  { nome: "AP", valor: "AMAPA" },
  { nome: "AM", valor: "AMAZONAS" },
  { nome: "BA", valor: "BAHIA" },
  { nome: "CE", valor: "CEARA" },
  { nome: "DF", valor: "DISTRITO_FEDERAL" },
  { nome: "ES", valor: "ESPIRITO_SANTO" },
  { nome: "GO", valor: "GOIAS" },
  { nome: "MA", valor: "MARANHAO" },
  { nome: "MT", valor: "MATO_GROSSO" },
  { nome: "MS", valor: "MATO_GROSSO_DO_SUL" },
  { nome: "MG", valor: "MINAS_GERAIS" },
  { nome: "PA", valor: "PARA" },
  { nome: "PB", valor: "PARAIBA" },
  { nome: "PR", valor: "PARANA" },
  { nome: "PE", valor: "PERNAMBUCO" },
  { nome: "PI", valor: "PIAUI" },
  { nome: "RJ", valor: "RIO_DE_JANEIRO" },
  { nome: "RN", valor: "RIO_GRANDE_DO_NORTE" },
  { nome: "RS", valor: "RIO_GRANDE_DO_SUL" },
  { nome: "RO", valor: "RONDONIA" },
  { nome: "RR", valor: "RORAIMA" },
  { nome: "SC", valor: "SANTA_CATARINA" },
  { nome: "SP", valor: "SAO_PAULO" },
  { nome: "SE", valor: "SERGIPE" },
  { nome: "TO", valor: "TOCANTINS" },
];

const formatCEP = (value: string): string => {
  value = value.replace(/\D/g, "");
  value = value.replace(/^(\d{5})(\d)/, "$1-$2");
  return value;
};

const Emprestimo = ({ params }: { params: { id: string } }) => {
  const router = useRouter();
  const token = Cookie.get("token");
  let decoded = "";
  if (token) {
    decoded = jwtDecode(token);
  }
  const [dataInicio, setDataInicio] = React.useState<Date>();
  const [dataFim, setDataFim] = React.useState<Date>();
  const [timeInicio, setTimeInicio] = useState("");
  const [timeFim, setTimeFim] = useState("");
  const [obs, setObs] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [emailProfessor, setEmailProfessor] = useState("");
  const [itensDisplay, setItensDisplay] = useState<ItemDisplay[]>([]);
  const [allItens, setAllItens] = useState<ItemDisplay[]>([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [open, setOpen] = React.useState(false);
  const [selectedEstado, setSelectedEstado] = useState<string>("");
  const [cep, setCep] = useState<string>("");
  const [cidade, setCidade] = useState<string>("");
  const [bairro, setBairro] = useState<string>("");
  const [rua, setRua] = useState<string>("");
  const [numero, setNumero] = useState<number>(0);
  const [complemento, setComplemento] = useState<string>("");

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
        quantidade: 1,
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

  const filteredItems = allItens.filter((item) =>
    item.nome.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleNewItem = (
    id: string,
    nome: string,
    qtde: number,
    tipo_item: string
  ) => {
    const newItem: ItemDisplay = {
      id: id,
      nome: nome,
      quantidade: qtde,
      tipo_item: tipo_item,
    };
    setItensDisplay([...itensDisplay, newItem]);
    setOpen(false);
  };

  const handleQuantityChange = (id: string, newQuantity: number) => {
    if (isNaN(newQuantity) || newQuantity < 0) return;
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

  const handleChangeCep = (e: ChangeEvent<HTMLInputElement>) => {
    const formattedCEP = formatCEP(e.target.value);
    setCep(formattedCEP);
  };

  const handleChangeEstado = (value: string) => {
    setSelectedEstado(value);
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    const professor = { email: emailProfessor };
    const simplifiedList: ItemSend[] = itensDisplay.map((item) => ({
      id: item.id,
      quantidade_transacao: item.quantidade,
    }));
    const userMailObj: EmailObject = { email: `${decoded.sub}` };
    const dateInicioFormat = format(dataInicio ? dataInicio : "", "dd/MM/yyyy");
    const dateFimFormat = format(dataFim ? dataFim : "", "dd/MM/yyyy");
    const dataHoraInicio = `${dateInicioFormat} ${timeInicio}:00`;
    const dataHoraFim = `${dateFimFormat} ${timeFim}:00`;
    try {
      const response = await fetch("http://localhost:8080/emprestimo", {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          dataHoraInicio,
          dataHoraFim,
          itens: simplifiedList,
          solicitante: userMailObj,
          observacaoSolicitacao: obs,
          professor: professor,
          endereco: {
            cep,
            uf: selectedEstado,
            cidade,
            bairro,
            rua,
            numero,
            complemento,
          },
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
        <TopMenu title="Solicitar empréstimo" />
      </div>
      <div className="pt-12">
        <form
          onSubmit={handleSubmit}
          className="bg-white p-8 rounded-md w-[600px] m-auto"
        >
          <div className="space-y-4">
            <div className="flex space-x-4 items-center">
              <Popover>
                <PopoverTrigger asChild>
                  <div className="flex flex-col">
                    <Label htmlFor="calendario" className="mb-1">
                      Data de retirada
                    </Label>
                    <Button
                      name="calendario"
                      variant={"outline"}
                      className={cn(
                        "w-[210px] justify-start text-left font-normal rounded-xl",
                        !dataInicio && "text-muted-foreground"
                      )}
                    >
                      <CalendarIcon className="mr-2 h-4 w-4" />
                      {dataInicio ? (
                        format(dataInicio, "dd/MM/yyyy")
                      ) : (
                        <span>Selecione a data</span>
                      )}
                    </Button>
                  </div>
                </PopoverTrigger>
                <PopoverContent className="w-auto p-0">
                  <Calendar
                    mode="single"
                    selected={dataInicio}
                    onSelect={setDataInicio}
                    initialFocus
                  />
                </PopoverContent>
              </Popover>
              <div className="flex flex-col items-start">
                <Label htmlFor="horaInicio" className="mb-1">Horário de retirada</Label>
                <Input
                  placeholder="00:00"
                  value={timeInicio}
                  onChange={handleChangeInicio}
                  name="horaInicio"
                />
              </div>
            </div>
            <div className="flex space-x-4">
              <Popover>
                <PopoverTrigger asChild>
                  <div className="flex flex-col">
                    <Label htmlFor="calendario" className="mb-1">
                      Data de entrega
                    </Label>
                    <Button
                      name="calendario"
                      variant={"outline"}
                      className={cn(
                        "w-[210px] justify-start text-left font-normal rounded-xl",
                        !dataInicio && "text-muted-foreground"
                      )}
                    >
                      <CalendarIcon className="mr-2 h-4 w-4" />
                      {dataFim ? (
                        format(dataFim, "dd/MM/yyyy")
                      ) : (
                        <span>Selecione a data</span>
                      )}
                    </Button>
                  </div>
                </PopoverTrigger>
                <PopoverContent className="w-auto p-0">
                  <Calendar
                    mode="single"
                    selected={dataFim}
                    onSelect={setDataFim}
                    initialFocus
                  />
                </PopoverContent>
              </Popover>
              <div className="flex flex-col items-start">
                <Label htmlFor="horaFim" className="mb-1">Horário de entrega</Label>
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
                type="email"
              />
            </div>
            <div>
              <div className="space-x-4">
                <Label htmlFor="itens">Itens no empréstimo</Label>
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
                    <div className="overflow-y-scroll space-y-5">
                      {filteredItems.map((item) => (
                        <div
                          onClick={() =>
                            handleNewItem(
                              item.id,
                              item.nome,
                              item.quantidade,
                              item.tipo_item
                            )
                          }
                          className="cursor-pointer"
                          key={item.id}
                        >
                          <SimplifiedItem
                            id={item.id}
                            nome={item.nome}
                            quantidade={item.quantidade}
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
                      value={item.quantidade}
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
            <div className="flex flex-wrap gap-2">
              <div>
                <Label htmlFor="cep">CEP</Label>
                <Input
                  name="cep"
                  value={cep}
                  placeholder="00000-000"
                  onChange={handleChangeCep}
                  maxLength={9}
                  className="w-28"
                />
              </div>
              <div>
                <Label htmlFor="uf">Estado</Label>
                <Select onValueChange={handleChangeEstado}>
                  <SelectTrigger className="w-[100px] rounded-xl">
                    <SelectValue placeholder="UF" />
                  </SelectTrigger>
                  <SelectContent>
                    {estados.map((estado) => (
                      <SelectItem value={estado.valor} key={estado.valor}>
                        {estado.nome}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              <div>
                <Label htmlFor="cidade">Cidade</Label>
                <Input
                  name="cidade"
                  placeholder="Nome cidade"
                  value={cidade}
                  onChange={(e) => setCidade(e.target.value)}
                  className="w-80"
                />
              </div>
              <div>
                <Label htmlFor="bairro">Bairro</Label>
                <Input
                  name="bairro"
                  placeholder="Nome bairro"
                  value={bairro}
                  onChange={(e) => setBairro(e.target.value)}
                
                />
              </div>
              <div>
                <Label htmlFor="rua">Rua</Label>
                <Input
                  name="rua"
                  placeholder="Nome rua"
                  value={rua}
                  onChange={(e) => setRua(e.target.value)}
                  className="w-96"
                />
              </div>
              <div>
                <Label htmlFor="numero">Numero</Label>
                <Input
                  name="numero"
                  type="number"
                  placeholder="000"
                  value={numero}
                  onChange={(e) => setNumero(parseInt(e.target.value, 10))}
                  className="w-20"
                />
              </div>
              <div>
                <Label htmlFor="complemento">Complemento</Label>
                <Input
                  name="complemento"
                  placeholder="Complemento"
                  value={complemento}
                  onChange={(e) => setComplemento(e.target.value)}
                  className="w-52"
                />
              </div>
            </div>

            <Button type="submit">Confirmar empréstimo</Button>

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

export default Emprestimo;
