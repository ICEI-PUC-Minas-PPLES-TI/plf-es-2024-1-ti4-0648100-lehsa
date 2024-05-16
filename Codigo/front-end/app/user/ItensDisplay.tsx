"use client";

import React, { useEffect, useState } from "react";
import Cookie from "js-cookie";
import SingleItemCard from "@/components/SingleItemCard";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from "@/components/ui/command";

import { Calendar } from "@/components/ui/calendar";
import { Button } from "@/components/ui/button";
import { CalendarIcon, Check, ChevronsUpDown, X } from "lucide-react";
import { Textarea } from "@/components/ui/textarea";
import { format } from "date-fns";
import { cn } from "@/lib/utils";
import { Input } from "@/components/ui/input";
import { jwtDecode } from "jwt-decode";
import { Label } from "@/components/ui/label";

interface ItensCardProps {
  searchTerm: string;
  onSubmitSuccess: () => void;
}

type Props = {
  id: string;
  img: File;
  nome: string;
  quantidade: number;
  tipo_item: string;
};

const ItensDisplay = ({ searchTerm, onSubmitSuccess }: ItensCardProps) => {
  const [items, setItems] = useState<Props[]>([]);
  const [data, setData] = React.useState<Date>();
  const [timeInicio, setTimeInicio] = useState("");
  const [timeFim, setTimeFim] = useState("");
  const [obs, setObs] = useState("");
  const [idItem, setIdItem] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [open, setOpen] = useState(false);
  const token = Cookie.get("token");

  const [openSolic, setOpenSolic] = React.useState(false);
  const [valueSolic, setValueSolic] = React.useState("");
  const [emails, setEmails] = useState([])
  const [solicitantes, setSolicitantes] = useState([])

  useEffect(() => {
    fetch("http://localhost:8080/item", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setItems(data);
      })
      .catch((error) => console.error("Error fetching items:", error));

      fetchEmails()
  }, []);


  const fetchEmails = async () => {
    const dataEmailsRaw = await fetch("http://localhost:8080/usuario/emails", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    })
    const dataEmails = await dataEmailsRaw.json()
    setEmails(dataEmails)
  }
  const emailsToDisplay = Array.isArray(emails) ? emails.slice(0, 5) : [];

  const handleRemoveEmail = (mail: string) => {
    setSolicitantes(prevEmails => prevEmails.filter(item => item !== mail));
  };

  const filteredItems = items.filter((item) =>
    item.nome.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleOpenerId = (id: string) => {
    setIdItem(id);
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

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    let decoded = "";
    if (token) {
      decoded = jwtDecode(token);
    }

    try {
      const dateFormat = format(data ? data : "", "dd/MM/yyyy");
      const dataHoraInicio = `${dateFormat} ${timeInicio}:00`;
      const dataHoraFim = `${dateFormat} ${timeFim}:00`;
      const solicitantes = [{ email: decoded.sub }];
      const itens = [{ id: idItem, quantidade_transacao: 1 }];
      const observacaoSolicitacao = obs;
      const response = await fetch("http://localhost:8080/agendamento", {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          dataHoraInicio,
          dataHoraFim,
          itens,
          solicitantes,
          observacaoSolicitacao,
        }),
      });
      if (!response.ok) {
        return setErrorMessage("Erro ao enviar formulário");
      }
      setOpen(false);
      onSubmitSuccess();
    } catch (error: any) {
      console.error("Erro ao enviar o formulário:", error.message);
    }
  };

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
      {filteredItems.map((item: Props) => (
        <Dialog open={open} onOpenChange={setOpen} key={item.id}>
          <DialogTrigger onClick={() => handleOpenerId(item.id)}>
            <SingleItemCard
              id={item.id}
              nome={item.nome}
              tipo_item={item.tipo_item}
              quantidade={item.quantidade}
            />
          </DialogTrigger>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Solicitação de agendamento</DialogTitle>
              <DialogDescription>
                This action cannot be undone. This will permanently delete your
                account and remove your data from our servers.
              </DialogDescription>
            </DialogHeader>
            <div>
              <form onSubmit={handleSubmit}>
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
                  <Popover open={openSolic} onOpenChange={setOpenSolic}>
                    <PopoverTrigger asChild>
                      <Button
                        variant="outline"
                        role="combobox"
                        aria-expanded={openSolic}
                        className="w-[200px] justify-between"
                      >
                        Digite um email...
                        <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                      </Button>
                    </PopoverTrigger>
                    <PopoverContent className="w-[200px] p-0">
                      <Command>
                        <CommandInput placeholder="Search framework..." />
                        <CommandList>
                          <CommandEmpty>No results found.</CommandEmpty>
                          {emailsToDisplay.map((email) => (
                            <CommandItem
                              key={email}
                              value={email}
                              onSelect={(currentValue) => {
                                setValueSolic(
                                  currentValue === valueSolic ? "" : currentValue
                                );
                                setOpenSolic(false);
                                setSolicitantes(prevEmails => [...prevEmails, email])
                              }}
                            >

                              {email}
                            </CommandItem>
                          ))}
                        </CommandList>
                      </Command>
                    </PopoverContent>
                  </Popover>
                  {solicitantes.map((mail) => (
                    <div className="flex space-x-2 items-center">
                      <span className="text-[14px]">{mail}</span>
                      <X className="cursor-pointer w-5" onClick={() => handleRemoveEmail(mail)}/>
                    </div>
                  ))}
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
          </DialogContent>
        </Dialog>
      ))}
    </div>
  );
};

export default ItensDisplay;
