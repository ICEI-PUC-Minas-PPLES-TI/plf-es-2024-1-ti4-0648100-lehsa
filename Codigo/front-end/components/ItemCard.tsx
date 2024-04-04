'use client'

import React, {useEffect, useState} from "react";
import Image from "next/image";
import {
    Card,
    CardContent,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import items from "../app/data.js";
import Link from "next/link";

type Props = {
    id: number;
    img: string;
    nome: string;
    quantidade: number;
    categoria: string;
};

type CategoryMappings = {
    [key: string]: string; // Isso indica que as chaves e valores são strings
};

const ItensCard = () => {

    const categoryMappings: CategoryMappings = {
        'EQUIPAMENTO': 'Equipamento'
        // Adicione mais mapeamentos conforme necessário
    };

    const [items, setItems] = useState([]);

    useEffect(() => {
        fetch("http://localhost:8080/itens", {
            method: 'GET',
            headers: {
                'Authorization': `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c3VhcmlvMDNAZXhhbXBsZS5jb20iLCJleHAiOjE3MTI0OTg4MTd9.N9H9vM4ZCrX61COnZJ-7sK1tw3n0xz4PP3BRr8CEfFguYwqktjGp5B5aZ_hAO_MpcSY0rIzqV9lVNvAR3USkcQ`,
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => setItems(data))
            .catch(error => console.error('Error fetching items:', error));
    }, []);

    return (
        <div className="grid grid-cols-4 gap-4 pl-60">
            {items.map((item: Props) => (
                <Card
                    key={item.id}
                    className="rounded-lg overflow-hidden shadow-lg w-80 max-w-80 mx-auto hover:shadow-xl transition-all duration-200 mt-10 ml-10"
                >
                    <Link href='/admin/itens/detalhes'>
                    <Image
                        alt="item picture"
                        className="object-cover w-full max-h-60"
                        height="320"
                        src="../images/placeholder.svg"
                        style={{
                            aspectRatio: "320/320",
                            objectFit: "cover",
                        }}
                        width="320"
                    />
                    <CardHeader>
                        <CardTitle>{item.nome}</CardTitle>
                    </CardHeader>
                    <CardContent>
                        <div className="grid grid-cols-2 items-center gap-4 text-sm">
                            <div className="font-medium">Categoria</div>
                            <div className="text-right">{categoryMappings[item.categoria]}</div>
                            <div className="col-span-2 border-t border-gray-200 dark:border-gray-800"/>
                            <div className="font-medium">Quantidade</div>
                            <div className="text-right">{item.quantidade}</div>
                        </div>
                    </CardContent>
                    </Link>
                </Card>
            ))}
        </div>
    );
};

export default ItensCard;
