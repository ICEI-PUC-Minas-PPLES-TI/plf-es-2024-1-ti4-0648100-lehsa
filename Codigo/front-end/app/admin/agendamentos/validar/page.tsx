import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import React from "react";
import {Button} from "@/components/ui/button";
import {Separator} from "@/components/ui/separator";
import Link from "next/link";

const ValidarAgendamento = () => {
    return (
        <>
            <main>
                <div className="container mx-auto px-4 py-8">
                    <div className="bg-white dark:bg-gray-950 rounded-lg shadow overflow-hidden">
                        <div className="px-6 py-8">
                            <div className="flex items-center justify-between mb-6">
                                <div>
                                    <h2 className="text-xl font-bold">Agendamento #1</h2>
                                    <p className="text-gray-500">11/04/2024</p>
                                </div>
                                <div className="flex items-center space-x-4">
                                    <Select>
                                        <SelectTrigger className="w-[180px]">
                                            <SelectValue placeholder="Técnico"/>
                                        </SelectTrigger>
                                        <SelectContent>
                                            <SelectItem value="jane">Lucca</SelectItem>
                                            <SelectItem value="michael">Vitor</SelectItem>
                                            <SelectItem value="sarah">Lucas</SelectItem>
                                        </SelectContent>
                                    </Select>
                                    <Button type="submit" className="w-full sm:w-auto bg-green-500 hover:bg-green-600 text-white font-bold py-2 px-4 rounded-lg focus:outline-none focus:shadow-outline">
                                        Aprovar</Button>
                                </div>
                            </div>
                            <Separator/>
                            <div className="grid grid-cols-2 gap-4 mt-4">
                                <div>
                                    <h3 className="text-lg font-bold mb-2">Item</h3>
                                    <ul className="text-gray-500 dark:text-gray-400">
                                        <li className="flex space-x-2">
                                            <h2 className="font-bold">Nome do item:</h2>
                                            <p>Impressora 3D</p>
                                        </li>

                                        <li className="flex space-x-2">
                                            <h2 className="font-bold">Categoria:</h2>
                                            <p>Equipamento</p>
                                        </li>

                                        <li className="flex space-x-2">
                                            <h2 className="font-bold">Data de início:</h2>
                                            <p>19/04/2024</p>
                                        </li>

                                        <li className="flex space-x-2">
                                            <h2 className="font-bold">Data final:</h2>
                                            <p>20/04/2024</p>
                                        </li>

                                        <li className="flex space-x-2">
                                            <h2 className="font-bold">Status:</h2>
                                            <span
                                                className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-yellow-100 text-yellow-800 dark:bg-yellow-800 dark:text-yellow-100">
                                                Pendente
                                            </span>
                                        </li>
                                    </ul>
                                </div>
                                <div>
                                    <h3 className="text-lg font-bold mb-2">Informações do usuário</h3>
                                    <ul className="text-gray-500 dark:text-gray-400">
                                    <li className="flex space-x-2">
                                            <h2 className="font-bold">Nome:</h2>
                                            <p>Maria Eduarda</p>
                                        </li>
                                        <li className="flex space-x-2">
                                            <h2 className="font-bold">Email:</h2>{" "}
                                            <Link className="text-blue-500 hover:underline" href="#">
                                                123@gmail.com
                                            </Link>
                                        </li>
                                        <li className="flex space-x-2">
                                            <h2 className="font-bold">Contato:</h2>
                                            <p>31 98767-8760</p>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </>
    )
}

export default ValidarAgendamento