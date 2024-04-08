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
        <div className="container mx-auto px-4 py-8 max-w-4xl bg-white rounded-xl shadow-xl border border-gray-100">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-8 items-center">
                <div className="flex justify-center">
                    <Image
                        alt="Product Image"
                        className="w-full max-w-xs h-auto object-cover rounded-lg shadow-lg"
                        src="/images/placeholder.svg"
                        width="300"
                        height="300"
                    />
                </div>
                <div className="flex flex-col gap-4">
                    <h1 className="font-bold text-3xl md:text-4xl text-gray-800">{item.nome}</h1>
                    <h2 className="font-semibold text-xl text-gray-500">Valor do item</h2>
                    <p className="text-2xl text-green-600">R${item.valor_unitario}</p>
                    <div className="flex gap-4">
                        <Link href={`/admin/itens/editar/${item.id}`}>
                            <div className="p-2 bg-yellow-500 rounded-full text-white hover:bg-yellow-600 transition duration-150 ease-in-out">
                                <EditIcon className="h-6 w-6"/>
                            </div>
                        </Link>
                        <button
                            onClick={handleDelete}
                            className="p-2 bg-red-600 rounded-full text-white hover:bg-red-700 transition duration-150 ease-in-out">
                            <TrashIcon className="h-6 w-6"/>
                        </button>
                    </div>
                </div>
            </div>
            <hr className="my-8 border-gray-200"/>
            <ul className="space-y-4">
                <li>
                    <h2 className="text-lg font-semibold">Tipo do item</h2>
                    <p className="text-md text-gray-600">{item.tipo_item}</p>
                </li>
                <li>
                    <h2 className="text-lg font-semibold">Quantidade</h2>
                    <p className="text-md text-gray-600">{item.quantidade}</p>
                </li>
                <li>
                    <h2 className="text-lg font-semibold">Emprestável</h2>
                    <p className="text-md text-gray-600">{item.emprestavel ? "Sim" : "Não"}</p>
                </li>
            </ul>
        </div>
    )
}
