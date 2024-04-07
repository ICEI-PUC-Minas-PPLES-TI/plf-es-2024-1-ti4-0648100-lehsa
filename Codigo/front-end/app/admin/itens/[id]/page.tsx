'use client'

import Image from "next/image";
import React, {useEffect, useState} from "react";
import { Separator } from "@/components/ui/separator";
import {EditIcon, TrashIcon} from "lucide-react";
import Link from "next/link";

type Props = {
    id: string;
    img: string;
    nome: string;
    quantidade: number;
    tipo_item: string;
    valor_unitario: number;
    emprestavel: boolean;
};
const fetchItem = async (id: string | string[]) => {
    try {
        const response = await fetch(`http://localhost:8080/item/${id}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c3VhcmlvMDNAZXhhbXBsZS5jb20iLCJleHAiOjE3MTI3MjgxOTN9.42cBdN7Fnd81t8oroFGRAyAbKWjPoWsvSGq5puDR0d5Gbkh2faWUaS09KHe64B-vi3ZhEEdZT_kq-i1QXrlXEA`,
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error("Failed to fetch item");
        }

        return await response.json();
    } catch (error) {
        console.error("Failed to fetch item:", error);
        return null;
    }
};

const deleteItem = async (id: string | string[]) => {
    try {
        const response = await fetch(`http://localhost:8080/item/${id}`, {
            method: "DELETE",
            headers: {
                Authorization:
                    "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c3VhcmlvMDNAZXhhbXBsZS5jb20iLCJleHAiOjE3MTI3MjgxOTN9.42cBdN7Fnd81t8oroFGRAyAbKWjPoWsvSGq5puDR0d5Gbkh2faWUaS09KHe64B-vi3ZhEEdZT_kq-i1QXrlXEA",
                "Content-Type": "application/json",
            },
        });

        if (!response.ok) {
            throw new Error("Failed to delete item");
        }

        return true;
    } catch (error) {
        console.error("Failed to delete item:", error);
        return false;
    }
};

export default function ItemDetails() {

    const [item, setItem] = useState<Props | null>(null);

    useEffect(() => {
        const fetchItemData = async () => {
            const router = window.location.pathname;
            const id = router.split('/')[3];

            if (!id) return;

            const data = await fetchItem(id);
            if (data) setItem(data);
            console.log(data)
        };

        fetchItemData();
    }, []);

    const handleDelete = async () => {
        const id = item?.id;
        if (!id) return;

        const deleted = await deleteItem(id);
        if (deleted) {
            window.history.back();
        }
    };

    if (!item) return <div>Loading...</div>;

    return (
        <div className="grid items-start px-4 gap-6 lg:gap-12 py-6 ml-80 mr-60 bg-card rounded-2xl">
            <div className="grid gap-4 items-start w-full md:w-auto">
                <div className="flex flex-col md:flex-row items-start md:items-center space-y-4 md:space-x-6">
                    <div className="grid gap-4">
                        <Image
                            alt="Product Image"
                            className="aspect-square object-cover border border-gray-200 rounded-lg overflow-hidden dark:border-gray-800"
                            height={200}
                            src="/images/placeholder.svg"
                            width={200}
                        />
                    </div>
                    <div className="grid gap-4">
                        <h1 className="font-bold text-2xl sm:text-3xl">{item.nome}</h1>
                        <h2 className="font-bold text-xl">Valor do item</h2>
                        <p className="text-xl">R${item.valor_unitario}</p>
                        <div className="flex flex-row gap-4">
                            <Link href={`/admin/itens/editar/${item.id}`}> <EditIcon className="text-yellow-600"/> </Link>
                            <TrashIcon className="text-red-800"
                            onClick={handleDelete}
                            style={{ cursor: "pointer" }}/>
                        </div>
                    </div>
                </div>
                <Separator className="border-gray-200 dark:border-gray-800"/>
                <ul className="grid gap-4">
                    <li className="space-y-2">
                        <h2 className="text-lg font-semibold">Tipo do item</h2>
                        <p className="text-md text-gray-500 dark:text-gray-400">{item.tipo_item}</p>
                    </li>
                    <li className="space-y-2">
                        <h2 className="text-lg font-semibold">Quantidade</h2>
                        <p className="text-md text-gray-500 dark:text-gray-400">{item.quantidade}</p>
                    </li>
                    <li className="space-y-2">
                        <h2 className="text-lg font-semibold">Emprestavel</h2>
                        <p className="text-md text-gray-500 dark:text-gray-400">{item.emprestavel ? "Sim" : "Não"}</p>
                    </li>
                </ul>
            </div>
        </div>
    )
}
