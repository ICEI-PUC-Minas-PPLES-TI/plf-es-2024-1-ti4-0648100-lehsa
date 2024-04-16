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

type Props = {
    id: number;
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
                    <Card className="rounded-lg overflow-hidden shadow-lg mx-auto hover:shadow-xl transition-all duration-200 mt-10">
                        <ImageComp
                            src={`http://localhost:8080/item/img/${item.id}`}
                            alt="item picture"
                            width={400}
                            height={400}
                            className="object-cover w-full max-h-60"                         
                        />
                        <CardHeader>
                            <CardTitle>{item.nome}</CardTitle>
                        </CardHeader>
                        <CardContent>
                            <div className="grid grid-cols-2 items-center gap-4 text-sm">
                                <div className="font-medium">{item.tipo_item}</div>
                                <div className="col-span-2 border-t border-gray-200 dark:border-gray-800" />
                                <div className="font-medium">Quantidade</div>
                                <div className="text-right">{item.quantidade}</div>
                            </div>
                        </CardContent>
                    </Card>
                </Link>
            ))}
        </div>
    );
};

export default ItensCard;
