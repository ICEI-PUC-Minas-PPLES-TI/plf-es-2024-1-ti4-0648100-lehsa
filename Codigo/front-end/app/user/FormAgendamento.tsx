'use client'

import React, { useEffect, useState } from 'react'
import {
    Popover,
    PopoverContent,
    PopoverTrigger,
} from "@/components/ui/popover"
import { Calendar } from "@/components/ui/calendar"
import { Button } from '@/components/ui/button';
import { CalendarIcon, Check, ChevronsUpDown } from 'lucide-react';
import { Textarea } from "@/components/ui/textarea"
import { format } from "date-fns"
import { cn } from '@/lib/utils';
import { Input } from '@/components/ui/input';
import {
    Command,
    CommandEmpty,
    CommandGroup,
    CommandInput,
    CommandItem,
} from "@/components/ui/command"
import Cookie from 'js-cookie'
import { jwtDecode } from 'jwt-decode';
import { Label } from '@/components/ui/label';

type Props = {
    id: string;
    img: File;
    nome: string;
    quantidade: number;
    tipo_item: string;
};

interface Params {
    id: string
}

const FormAgendamento = ({ id }: Params) => {

    const [data, setData] = React.useState<Date>()
    const [timeInicio, setTimeInicio] = useState('')
    const [timeFim, setTimeFim] = useState('')
    const [obs, setObs] = useState('')
    const [errorMessage, setErrorMessage] = useState('')


    const token = Cookie.get("token");

    const handleChangeInicio = (event: React.ChangeEvent<HTMLInputElement>) => {
        const value = event.target.value;
        let cleanValue = value.replace(/[^0-9]/g, '');
        cleanValue = cleanValue.slice(0, 4);
        if (cleanValue.length > 2) {
            cleanValue = cleanValue.slice(0, 2) + ':' + cleanValue.slice(2);
        }
        setTimeInicio(cleanValue);
    };

    const handleChangeFim = (event: React.ChangeEvent<HTMLInputElement>) => {
        const value = event.target.value;
        let cleanValue = value.replace(/[^0-9]/g, '');
        cleanValue = cleanValue.slice(0, 4);
        if (cleanValue.length > 2) {
            cleanValue = cleanValue.slice(0, 2) + ':' + cleanValue.slice(2);
        }
        setTimeFim(cleanValue);
    };

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        let decoded = ''
        if (token) {
            decoded = jwtDecode(token);
        }

        try {
            const dateFormat = format(data ? data : '', "dd/MM/yyyy")
            const dataHoraInicio = `${dateFormat} ${timeInicio}:00`
            const dataHoraFim = `${dateFormat} ${timeFim}:00`
            const solicitantes = [{ "email": decoded.sub }]
            const itens = [{ "id": id }]
            const observacaoSolicitacao = obs
            const response = await fetch('http://localhost:8080/agendamento', {
                method: 'POST',
                headers: {
                    "Authorization": `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    dataHoraInicio,
                    dataHoraFim,
                    itens,
                    solicitantes,
                    observacaoSolicitacao
                })
            });
            if (!response.ok) {
                setErrorMessage('Erro ao enviar formulário')
            }
            
        } catch (error: any) {
            console.error('Erro ao enviar o formulário:', error.message);
        }
    }

    return (
        <form onSubmit={handleSubmit}>
            <div className='space-y-4'>
                <Popover>
                    <PopoverTrigger asChild>
                        <div className='flex flex-col'>
                            <Label htmlFor='calendario'>Data desejada</Label>
                            <Button
                                name='calendario'
                                variant={"outline"}
                                className={cn(
                                    "w-[280px] justify-start text-left font-normal",
                                    !data && "text-muted-foreground"
                                )}
                            >
                                <CalendarIcon className="mr-2 h-4 w-4" />
                                {data ? format(data, "dd/MM/yyyy") : <span>Selecione a data</span>}
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
                <div className='flex space-x-4'>
                    <div>
                        <Label htmlFor='horaInicio'>Horário de Início</Label>
                        <Input placeholder='00:00' value={timeInicio} onChange={handleChangeInicio} name='horaInicio' />
                    </div>
                    <div>
                        <Label htmlFor='horaFim'>Horário de Término</Label>
                        <Input placeholder='00:00' value={timeFim} onChange={handleChangeFim} name='horaFim' />
                    </div>
                </div>
                <div>
                    <Label htmlFor='observacoes'>Observações</Label>
                    <Textarea placeholder="Preencha com informações que julgar necessário" value={obs} onChange={e => setObs(e.target.value)} name='observacoes'/>
                </div>
                <Button type='submit'>Confirmar agendamento</Button>
                {errorMessage && <p className="font-semibold text-red-600 mt-3 text-center">{errorMessage}</p>}
            </div>
        </form>
    )
}

export default FormAgendamento