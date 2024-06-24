'use client'

import React, { useEffect, useState } from "react";
import Cookie from 'js-cookie'
import {
    Card,
    CardContent,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import Link from "next/link";
import ImageComp from "./ImageComp";
import SingleItemCard from "./SingleItemCard";

type Props = {
    id: string;
    img: File;
    nome: string;
    quantidade: number;
    tipo_item: string;
    emprestavel: boolean;
};

interface ItensCardProps {
    searchTerm: string;
}

const ItensCard = ({ searchTerm }: ItensCardProps) => {

    const [items, setItems] = useState<Props[]>([]);
    const token = Cookie.get("token");

    useEffect(() => {
        fetch(`${process.env.NEXT_PUBLIC_API_URL}/item`, {
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
        <div className="flex flex-wrap gap-4">
            {filteredItems.map((item: Props) => (
                <Link key={item.id} href={`/admin/itens/${item.id}`}>
                    <SingleItemCard id={item.id} nome={item.nome} tipo_item={item.tipo_item} quantidade={item.quantidade} emprestavel={item.emprestavel}/>                    
                </Link>
            ))}
        </div>
    );
};

export default ItensCard;
