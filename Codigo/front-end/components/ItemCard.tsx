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
};

interface ItensCardProps {
    searchTerm: string;
}
const ItensCard = ({ searchTerm }: ItensCardProps) => {

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
                <Link key={item.id} href={`/admin/itens/${item.id}`}>
                    <SingleItemCard id={item.id} nome={item.nome} tipo_item={item.tipo_item} quantidade={item.quantidade}/>                    
                </Link>
            ))}
        </div>
    );
};

export default ItensCard;
