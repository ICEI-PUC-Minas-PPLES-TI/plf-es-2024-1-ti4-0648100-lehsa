'use client'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Switch } from '@/components/ui/switch'
import React, {useEffect, useState} from 'react'
import { useRouter} from 'next/navigation';

function getIdFromUrl(url: string) {
    const segments = url.split('/');
    return segments[segments.length - 1];
}

const EditarItem = () => {

    const [tipoSelecionado, setTipoSelecionado] = useState('');
    const handleTipoSelecionado = (valor: string) => {
        setTipoSelecionado(valor);
    };

    const router = useRouter();
    const [item, setItem] = useState(null);
    const consultaId = getIdFromUrl(window.location.pathname);

    useEffect(() => {
        const fetchItemData = async () => {
            console.log(consultaId)
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
            const emprestavel = formData.get('emprestavel')
            const tipo_item = tipoSelecionado
            console.log(tipo_item)


            const response = await fetch(`http://localhost:8080/item/${id}`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c3VhcmlvMDNAZXhhbXBsZS5jb20iLCJleHAiOjE3MTI0OTg4MTd9.N9H9vM4ZCrX61COnZJ-7sK1tw3n0xz4PP3BRr8CEfFguYwqktjGp5B5aZ_hAO_MpcSY0rIzqV9lVNvAR3USkcQ`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({nome, quantidade, valor_unitario, emprestavel, tipo_item})
            });

            if (!response.ok) {
                throw new Error("Failed to update item");
            }

            router.push(`/admin/itens/${id}`);
        } catch (error) {
            console.error("Failed to update item:", error);
        }
    };

    const options = [
        { label: 'EQUIPAMENTO', value: 'EQUIPAMENTO' },
        { label: 'VIDRARIA', value: 'VIDRARIA' },
    ];

    return (
        <div className='bg-white rounded-xl h-3/4 p-5 ml-60 w-4/12'>
            <h3 className='text-xl font-semibold'>Atualizar</h3>
            <form className="max-w-md" onSubmit={handleSubmit}>
            <Label htmlFor='nome'>Nome</Label>
            <Input placeholder='nome do item' name='nome' />
            <Label htmlFor="quantidade">Quantidade</Label>
            <Input placeholder='0' name='quantidade' type="number" />
            <Label htmlFor="valor_unitario">Valor</Label>
            <Input placeholder='R$ 000,00' name='valor_unitario' type="number"/>
            <div className='flex flex-col space-y-3'>
                <Label htmlFor='emprestavel'>Emprestavel?</Label>
                <Switch name='emprestavel' />
            </div>
            <Label htmlFor='tipo_item'>Categoria</Label>
            <Select>
                <SelectTrigger>
                    <SelectValue placeholder='Tipo'/>
                </SelectTrigger>
                <SelectContent>
                    {options.map((option, index) => (
                        <SelectItem key={index} value={option.value} onClick={() => handleTipoSelecionado(option.value)}>
                            {option.label}
                        </SelectItem>
                    ))}
                </SelectContent>
            </Select>
            <Button type="submit" className="bg-green-500 hover:bg-green-600 my-20 float-right">Salvar</Button>
            </form>
        </div>
    )
}

export default EditarItem