'use client'

import React, { useEffect, useState } from "react";
import { EditIcon, TrashIcon} from "lucide-react";
import Link from "next/link";
import Cookie from "js-cookie";
import ImageComp from "@/components/ImageComp";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
    AlertDialogTrigger,
} from "@/components/ui/alert-dialog"

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
        const token = Cookie.get("token");
        const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/item/${id}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                "Authorization": `Bearer ${token}`
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

        const token = Cookie.get("token");
        if (!token) {
            throw new Error("Usuário não autenticado");
        }

        const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/item/${id}`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
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
<div className="container mx-auto px-4 py-4 max-w-3xl bg-white rounded-lg shadow-md border border-gray-200">
    <div className="grid grid-cols-1 md:grid-cols-2 gap-6 items-center">
        <div className="flex justify-center">
            <ImageComp
                src={`${process.env.NEXT_PUBLIC_API_URL}/item/img/${item.id}`}
                alt="item picture"
                width={200}
                height={200}
                className="w-full max-w-xs h-auto cover rounded-lg shadow-md"
            />
        </div>
        <div className="flex flex-col gap-3">
            <h1 className="font-bold text-2xl md:text-xl text-gray-800">{item.nome}</h1>
            <h2 className="font-semibold text-lg text-gray-500">Valor do item</h2>
            <p className="text-lg text-green-600">R${item.valor_unitario}</p>
            <div className="flex gap-3">
                <Link href={`/admin/itens/editar/${item.id}`}>
                    <div className="p-2 bg-yellow-500 rounded-full text-white hover:bg-yellow-600 transition duration-150 ease-in-out">
                        <EditIcon className="h-5 w-5" />
                    </div>
                </Link>
                <AlertDialog>
                    <AlertDialogTrigger className="p-2 bg-red-600 rounded-full text-white hover:bg-red-700 transition duration-150 ease-in-out">
                        <TrashIcon className="h-5 w-5" />
                    </AlertDialogTrigger>
                    <AlertDialogContent>
                        <AlertDialogHeader>
                            <AlertDialogTitle>Tem certeza?</AlertDialogTitle>
                            <AlertDialogDescription>
                                Esta ação não pode ser revertida. Este Item será deletado permanentemente do sistema.
                            </AlertDialogDescription>
                        </AlertDialogHeader>
                        <AlertDialogFooter>
                            <AlertDialogCancel className="border-none">Cancelar</AlertDialogCancel>
                            <AlertDialogAction onClick={handleDelete} className="bg-red-600 hover:bg-red-700 transition duration-150 ease-in-out">Continuar</AlertDialogAction>
                        </AlertDialogFooter>
                    </AlertDialogContent>
                </AlertDialog>
            </div>
        </div>
    </div>
    <hr className="my-6 border-gray-200" />
    <ul className="space-y-3">
        <li>
            <h2 className="text-md font-semibold">Tipo do item</h2>
            <p className="text-sm text-gray-600">{item.tipo_item}</p>
        </li>
        <li>
            <h2 className="text-md font-semibold">Quantidade</h2>
            <p className="text-sm text-gray-600">{item.quantidade}</p>
        </li>
        <li>
            <h2 className="text-md font-semibold">Emprestável</h2>
            <p className="text-sm text-gray-600">{item.emprestavel ? "Sim" : "Não"}</p>
        </li>
    </ul>
</div>
    )
}
