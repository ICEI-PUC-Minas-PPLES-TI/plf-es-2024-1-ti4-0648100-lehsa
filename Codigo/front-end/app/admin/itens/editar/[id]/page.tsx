'use client'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Switch } from '@/components/ui/switch'
import React, { useEffect, useState } from 'react'
import { useRouter } from 'next/navigation';

function getIdFromUrl(url: string) {
    const segments = url.split('/');
    return segments[segments.length - 1];
}

const EditarItem = () => {

    const router = useRouter();
    const [item, setItem] = useState(null);
    const [tipoItem, setTipoItem] = useState("");
    const [itemEmprestavel, setItemEmprestavel] = useState("");
    const consultaId = getIdFromUrl(window.location.pathname);

    useEffect(() => {
        const fetchItemData = async () => {
            console.log(consultaId)
            try {
                const response = await fetch(`http://localhost:8080/item/${consultaId}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c3VhcmlvMDNAZXhhbXBsZS5jb20iLCJleHAiOjE3MTI3MjgxOTN9.42cBdN7Fnd81t8oroFGRAyAbKWjPoWsvSGq5puDR0d5Gbkh2faWUaS09KHe64B-vi3ZhEEdZT_kq-i1QXrlXEA`,
                        'Content-Type': 'application/json'
                    }
                });

                if (!response.ok) {
                    throw new Error("Failed to fetch item");
                }

                const data = await response.json();
                setItem(data);
            } catch (error) {
                console.error("Failed to fetch item:", error);
            }
        };


        if (consultaId) {
            fetchItemData();
        }
    }, [consultaId]);

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        const id = getIdFromUrl(window.location.pathname);
        event.preventDefault();

        try {
            if (!id) {
                console.error("Item ID is null");
                return;
            }

            const formData = new FormData(event.currentTarget);
            const nome = formData.get('nome')
            const quantidade = formData.get('quantidade')
            const valor_unitario = formData.get('valor_unitario')
            const emprestavel = itemEmprestavel
            const tipo_item = tipoItem

            const response = await fetch(`http://localhost:8080/item/${id}`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c3VhcmlvMDNAZXhhbXBsZS5jb20iLCJleHAiOjE3MTI3MjgxOTN9.42cBdN7Fnd81t8oroFGRAyAbKWjPoWsvSGq5puDR0d5Gbkh2faWUaS09KHe64B-vi3ZhEEdZT_kq-i1QXrlXEA`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ nome, quantidade, valor_unitario, emprestavel, tipo_item })
            });

            if (!response.ok) {
                throw new Error("Failed to update item");
            }

            router.push(`/admin/itens/${id}`);
        } catch (error) {
            console.error("Failed to update item:", error);
        }
    };

    return (
        <div className='bg-white shadow-lg rounded-xl p-8 ml-auto mr-auto mt-10 w-full max-w-4xl'>
            <h3 className='text-2xl font-semibold text-gray-800 mb-6'>Atualizar Item</h3>
            <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                    <Label htmlFor='nome'>Nome</Label>
                    <Input placeholder='Nome do item' name='nome' className="mt-1 block w-full"/>
                </div>
                <div>
                    <Label htmlFor="quantidade">Quantidade</Label>
                    <Input placeholder='0' name='quantidade' type="number" className="mt-1 block w-full"/>
                </div>
                <div>
                    <Label htmlFor="valor_unitario">Valor Unitário</Label>
                    <Input placeholder='R$ 000,00' name='valor_unitario' type="number" className="mt-1 block w-full"/>
                </div>
                <div>
                    <Label htmlFor='emprestavel'>Emprestável?</Label>
                    <select value={itemEmprestavel} onChange={(e) => setItemEmprestavel(e.target.value)}
                            className="mt-1 block w-full border-gray-300 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50 rounded-md">
                        <option value="">Selecione</option>
                        <option value="true">Sim</option>
                        <option value="false">Não</option>
                    </select>
                </div>
                <div>
                    <Label htmlFor='tipo_item'>Categoria</Label>
                    <select value={tipoItem} onChange={(e) => setTipoItem(e.target.value)}
                            className="mt-1 block w-full border-gray-300 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-500 focus:ring-opacity-50 rounded-md">
                        <option value="">Selecione</option>
                        <option value="EQUIPAMENTO">Equipamento</option>
                        <option value="VIDRARIA">Vidraria</option>
                    </select>
                </div>
                <Button type="submit" className="w-full sm:w-auto bg-green-500 hover:bg-green-600 text-white font-bold py-2 px-4 rounded-lg focus:outline-none focus:shadow-outline">
                    Salvar
                </Button>
            </form>
        </div>
    )
}

export default EditarItem
