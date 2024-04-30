'use client'

import React, { useEffect, useState } from 'react'
import Cookie from 'js-cookie'
import SingleItemCard from '@/components/SingleItemCard';
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog"
import FormAgendamento from './FormAgendamento';

interface ItensCardProps {
    searchTerm: string;
}

type Props = {
    id: string;
    img: File;
    nome: string;
    quantidade: number;
    tipo_item: string;
};

const ItensDisplay = ({ searchTerm }: ItensCardProps) => {

    const [items, setItems] = useState<Props[]>([]);
    const token = Cookie.get("token");

    useEffect(() => {
        fetch("http://localhost:8080/item", {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                "Authorization": `Bearer ${token}`
            }
        })
            .then(response => response.json())
            .then(data => {
                setItems(data)
            })
            .catch(error => console.error('Error fetching items:', error));
    }, []);

    const filteredItems = items.filter(item =>
        item.nome.toLowerCase().includes(searchTerm.toLowerCase())
    );

    

    return (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
            {filteredItems.map((item: Props) => (
                <Dialog>
                    <DialogTrigger >
                        <SingleItemCard id={item.id} nome={item.nome} tipo_item={item.tipo_item} quantidade={item.quantidade} />
                    </DialogTrigger>
                    <DialogContent>
                        <DialogHeader>
                            <DialogTitle>Solicitação de agendamento</DialogTitle>
                            <DialogDescription>
                                This action cannot be undone. This will permanently delete your account
                                and remove your data from our servers.
                            </DialogDescription>
                        </DialogHeader>
                        <div>
                            <FormAgendamento id={item.id} />
                            
                        </div>
                    </DialogContent>
                </Dialog>
            ))}
        </div>
    )
}

export default ItensDisplay