'use client'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Switch } from '@/components/ui/switch'
import React, {useEffect, useState} from 'react'
import { useRouter} from 'next/navigation';

interface Props {
    itemId: string;
}

const EditarItem = ({ itemId }: Props) => {

    const router = useRouter();
    const [item, setItem] = useState(null);
    const consultaId = window.location.href.split('/').pop();

    useEffect(() => {
        const fetchItemData = async () => {
            try {
                const response = await fetch(`http://localhost:8080/item/${consultaId}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c3VhcmlvMDNAZXhhbXBsZS5jb20iLCJleHAiOjE3MTI0OTg4MTd9.N9H9vM4ZCrX61COnZJ-7sK1tw3n0xz4PP3BRr8CEfFguYwqktjGp5B5aZ_hAO_MpcSY0rIzqV9lVNvAR3USkcQ`,
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
        event.preventDefault();

        try {
            if (!consultaId) {
                console.error("Item ID is null");
                return;
            }

            const formData = new FormData(event.currentTarget);
            const nome = formData.get('nome')
            const quantidade = formData.get('quantidade')
            const valor = formData.get('valor')
            const emprestavel = formData.get('emprestavel')
            const categoria = formData.get('categoria')


            const response = await fetch(`http://localhost:8080/item/${consultaId}`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c3VhcmlvMDNAZXhhbXBsZS5jb20iLCJleHAiOjE3MTI0OTg4MTd9.N9H9vM4ZCrX61COnZJ-7sK1tw3n0xz4PP3BRr8CEfFguYwqktjGp5B5aZ_hAO_MpcSY0rIzqV9lVNvAR3USkcQ`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({nome, quantidade, valor, emprestavel, categoria})
            });

            if (!response.ok) {
                throw new Error("Failed to update item");
            }

            // Redirecionar para a página de detalhe do item após a atualização
            router.push(`/admin/itens/${itemId}`);
        } catch (error) {
            console.error("Failed to update item:", error);
        }
    };

    return (
        <div className='bg-white rounded-xl h-3/4 p-5 ml-60 w-4/12'>
            <h3 className='text-xl font-semibold'>Atualizar</h3>
            <form className="max-w-md" onSubmit={handleSubmit}>
            <Label htmlFor='nome'>Nome</Label>
            <Input placeholder='nome do item' id='nome' />
            <Label htmlFor="quantidade">Quantidade</Label>
            <Input placeholder='0' id='quantidade' />
            <Label htmlFor="valor">Valor</Label>
            <Input placeholder='R$ 000,00' id='valor' />
            <div className='flex flex-col space-y-3'>
                <Label htmlFor='emprestavel'>Emprestavel?</Label>
                <Switch id='emprestavel' />
            </div>
            <Label htmlFor='categoria'>Categoria</Label>
            <Select>
                <SelectTrigger>
                    <SelectValue placeholder='Categoria'/>
                </SelectTrigger>
                <SelectContent>
                    <SelectItem value='material'>Material</SelectItem>
                    <SelectItem value='insumo'>Insumo</SelectItem>
                    <SelectItem value='equipamento'>Equipamento</SelectItem>
                    <SelectItem value='outro'>Outro</SelectItem>
                </SelectContent>
            </Select>
            <Button type="submit" className="bg-green-500 hover:bg-green-600 my-20 float-right">Salvar</Button>
            </form>
        </div>
    )
}

export default EditarItem