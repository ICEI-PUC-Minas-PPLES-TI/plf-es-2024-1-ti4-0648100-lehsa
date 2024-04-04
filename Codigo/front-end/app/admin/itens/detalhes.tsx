import { Button } from "@/components/ui/button"
import Image from "next/image";
import React from "react";
import { Separator } from "@/components/ui/separator";
import {DeleteIcon, EditIcon, TrashIcon} from "lucide-react";

export default function ItemDetails() {
    return (
        <div className="grid md:grid-cols-2 items-start px-4 gap-6 lg:gap-12 py-6 ml-80">
            <div className="grid gap-4 items-start w-full md:w-auto">
                <div className="flex flex-col md:flex-row items-start md:items-center space-y-4 md:space-x-6">
                    <div className="grid gap-4">
                        <Image
                            alt="Product Image"
                            className="aspect-square object-cover border border-gray-200 rounded-lg overflow-hidden dark:border-gray-800"
                            height={200}
                            src="../images/placeholder.svg"
                            width={200}
                        />
                    </div>
                    <div className="grid gap-4">
                        <h1 className="font-bold text-2xl sm:text-3xl">Acme Circles T-Shirt</h1>
                        <h2 className="font-bold text-xl">Valor do item</h2>
                        <p className="text-xl">R$ 200</p>
                    </div>
                </div>
                <Separator className="border-gray-200 dark:border-gray-800"/>
                <ul className="grid gap-4">
                    <li className="space-y-2">
                        <h2 className="text-lg font-semibold">Tipo do item</h2>
                        <p className="text-sm text-gray-500 dark:text-gray-400">Equipamento</p>
                    </li>
                    <li className="space-y-2">
                        <h2 className="text-lg font-semibold">Quantidade</h2>
                        <p className="text-sm text-gray-500 dark:text-gray-400">2</p>
                    </li>
                    <li className="space-y-2">
                        <h2 className="text-lg font-semibold">Emprestavel</h2>
                        <p className="text-sm text-gray-500 dark:text-gray-400">SIM</p>
                    </li>
                </ul>
            </div>

            <div className="flex gap-4">
                <EditIcon className="text-yellow-600"/>
                <TrashIcon className="text-red-800"/>
            </div>
        </div>
    )
}
