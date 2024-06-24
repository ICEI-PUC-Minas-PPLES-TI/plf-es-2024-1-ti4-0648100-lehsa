"use client";
import React, { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import Cookie from "js-cookie";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Switch } from "@/components/ui/switch";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import Image from "next/image";

const EditarItem = ({ params }: { params: { id: string } }) => {
  const router = useRouter();

  const [nome, setNome] = useState("");
  const [imagem, setImagem] = useState<File | null>(null);
  const [quantidade, setQuantidade] = useState("");
  const [valorUnitario, setValor] = useState("");
  const [emprestavel, setIsToggled] = useState(false);
  const [tipoItem, setSelectedOption] = useState<string>("");

  useEffect(() => {
    const fetchItemData = async () => {
      try {
        const token = Cookie.get("token");
        const response = await fetch(
          `http://localhost:8080/item/${params.id}`,
          {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (!response.ok) {
          throw new Error("Failed to fetch item");
        }

        const data = await response.json();
        setNome(data.nome);
        setQuantidade(data.quantidade.toString());
        setValor(data.valor_unitario.toString());
        setIsToggled(data.emprestavel);
        setSelectedOption(data.tipo_item);
      } catch (error) {
        console.error("Failed to fetch item:", error);
      }
    };

    if (params.id) {
      fetchItemData();
    }
  }, [params.id]);

  const [imageUrl, setImageUrl] = useState<string>("");

  useEffect(() => {
    const token = Cookie.get("token");
    if (token) {
      fetch(`http://localhost:8080/item/img/${params.id}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
        .then((response) => response.blob())
        .then((blob) => {
          const localUrl = URL.createObjectURL(blob);
          setImageUrl(localUrl);
        })
        .catch((error) => console.error("Erro carregar imagem:", error));
    }
  }, [`http://localhost:8080/item/img/${params.id}`]);

  const handleImageChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files) {
      setImagem(event.target.files[0]);
      const reader = new FileReader();
      reader.onloadend = () => {
        setImageUrl(reader.result as string);
      };
      reader.readAsDataURL(event.target.files[0]);
    }
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const formData = new FormData();
    formData.append(
      "item",
      new Blob(
        [
          JSON.stringify({
            nome,
            quantidade,
            valorUnitario,
            emprestavel,
            tipoItem,
          }),
        ],
        {
          type: "application/json",
        }
      )
    );
    if (imagem) {
      formData.append("imagem", imagem);
    }

    const token = Cookie.get("token");
    try {
      const response = await fetch(`http://localhost:8080/item/${params.id}`, {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${token}`,
        },
        body: formData,
      });
      if (!response.ok) {
        throw new Error("Falha ao enviar o formulário");
      }
      router.push("/admin/itens");
    } catch (error: any) {
      console.error("Erro ao enviar o formulário:", error.message);
    }
  };

  return (
    <div className="bg-white shadow-lg rounded-xl p-8 ml-auto mr-auto mt-10 w-full max-w-4xl">
      <h3 className="text-2xl font-semibold text-gray-800 mb-6">
        Atualizar Item
      </h3>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <Label htmlFor="nome">Nome</Label>
          <Input
            value={nome}
            placeholder="Nome do item"
            name="nome"
            className="mt-1 block w-full"
            onChange={(e) => setNome(e.target.value)}
          />
        </div>
        <div className="flex space-x-8">
          <div>
            <Label htmlFor="imagem">Imagem Atual</Label>
            {imageUrl && (
              <Image
                src={imageUrl}
                alt="Imagem do Item"
                width={200}
                height={200}
                className="rounded-md"
              />
            )}
          </div>
          <div>
            <Label htmlFor="imagem">Atualizar Imagem</Label>
            <Input
              name="imagem"
              type="file"
              accept="image/*"
              onChange={handleImageChange}
            />
          </div>
        </div>
        <div>
          <Label htmlFor="quantidade">Quantidade</Label>
          <Input
            value={quantidade}
            placeholder="0"
            name="quantidade"
            type="number"
            className="mt-1 block w-full"
            onChange={(e) => setQuantidade(e.target.value)}
          />
        </div>
        <div>
          <Label htmlFor="valor_unitario">Valor Unitário</Label>
          <Input
            value={valorUnitario}
            placeholder="R$ 000,00"
            name="valor_unitario"
            type="number"
            className="mt-1 block w-full"
            onChange={(e) => setValor(e.target.value)}
          />
        </div>
        <div className="flex flex-col space-y-3">
          <Label htmlFor="emprestavel">Emprestavel?</Label>
          <Switch
            name="emprestavel"
            checked={emprestavel}
            onCheckedChange={setIsToggled}
          />
        </div>
        <Select
          name="tipo_item"
          value={tipoItem}
          onValueChange={setSelectedOption}
        >
          <SelectTrigger>
            <SelectValue placeholder="Selecione o tipo do item" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="VIDRARIA">Vidraria</SelectItem>
            <SelectItem value="EQUIPAMENTO">Equipamento</SelectItem>
          </SelectContent>
        </Select>
        <Button
          type="submit"
          className="w-full sm:w-auto bg-green-500 hover:bg-green-600 text-white font-bold py-2 px-4 rounded-lg focus:outline-none focus:shadow-outline"
        >
          Salvar
        </Button>
      </form>
    </div>
  );
};

export default EditarItem;
