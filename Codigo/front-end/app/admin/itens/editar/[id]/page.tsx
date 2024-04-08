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
        <div className='bg-white rounded-xl h-3/4 p-5 ml-60 w-4/12'>
            <h3 className='text-xl font-semibold'>Atualizar</h3>
            <form className="max-w-md" onSubmit={handleSubmit}>
                <Label htmlFor='nome'>Nome</Label>
                <Input placeholder='nome do item' name='nome' />
                <Label htmlFor="quantidade">Quantidade</Label>
                <Input placeholder='0' name='quantidade' type="number" />
                <Label htmlFor="valor_unitario">Valor</Label>
                <Input placeholder='R$ 000,00' name='valor_unitario' type="number" />
                <div>
                    <Label htmlFor='emprestavel'>Emprestavel?</Label>
                    <select value={itemEmprestavel} onChange={(e) => setItemEmprestavel(e.target.value)}
                            className="border border-gray-300 rounded-md p-2 flex">
                        <option value="">Tipo</option>
                        <option value="true">Sim</option>
                        <option value="false">Não</option>
                    </select>
                </div>
                <Label htmlFor='tipo_item'>Categoria</Label>
                <select value={tipoItem} onChange={(e) => setTipoItem(e.target.value)}
                        className="border border-gray-300 rounded-md p-2 flex">
                    <option value="">Tipo</option>
                    <option value="EQUIPAMENTO">Equipamento</option>
                    <option value="VIDRARIA">Vidraria</option>
                </select>
                <Button type="submit" className="bg-green-500 hover:bg-green-600 my-20 float-right">Salvar</Button>
            </form>
        </div>
    )
}

export default EditarItem
