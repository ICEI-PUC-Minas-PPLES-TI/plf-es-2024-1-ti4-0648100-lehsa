'use client'
import { Button } from "@/components/ui/button";
import { Separator } from "@/components/ui/separator";
import Link from "next/link";
import Cookie from "js-cookie";
import { useEffect, useState } from "react";

type Solicitante = {
  id: string;
  nome: string;
  telefone: string;
  email: string;
  curso: string | null;
};

type Item = {
  id: string;
  nome: string;
  tipo_item: string;
  quantidade: number;
};

type Endereco = {
  id: string;
  cep: string;
  uf: string;
  cidade: string;
  bairro: string;
  rua: string;
  numero: string;
  complemento: string;
};

type Emprestimo = {
  id: string;
  statusTransacaoItem: String;
  dataHoraInicio: string;
  dataHoraFim: string;
  tecnico: string | null;
  solicitante: Solicitante;
  itens: Item[];
  endereco: Endereco;
  observacaoSolicitacao: string;
};

const fetchItem = async (id: string | string[]) => {
  try {
    const token = Cookie.get("token");
    const response = await fetch(`http://localhost:8080/emprestimo/${id}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
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

const DetalhesEmprestimo = () => {
  const [emprestimo, setEmprestimo] = useState<Emprestimo | null>(null);

  const handleAcao = async (status: String) => {
    try {
      const token = Cookie.get("token");
      const response = await fetch(
        `http://localhost:8080/emprestimo/${emprestimo?.id}/${status}`,
        {
          method: "PATCH",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        }
      );
  
      if (!response.ok) {
        throw new Error("Failed to update status");
      }

      setEmprestimo({
        ...emprestimo!,
        statusTransacaoItem: status,
      });

    } catch (error) {
      console.error("Error updating status:", error);
    }
  };

  useEffect(() => {
    const fetchItemData = async () => {
      const router = window.location.pathname;
      const id = router.split('/')[3];
      const status = router.split('/')[4];

      if (!id) return;

      const data = await fetchItem(id);
      setEmprestimo(data);

      if (status) {
        handleAcao(status);
      }

      console.log(data)
  };

  fetchItemData();
  }, []);

  return (
    <>
      <main>
        <div className="container mx-auto px-4 py-8">
          <div className="bg-white dark:bg-gray-950 rounded-lg shadow overflow-hidden">
            <div className="px-6 py-8">
              <div className="flex items-center justify-between mb-6">
                <div>
                  <h2 className="text-xl font-bold mb-2">
                    Detalhes do empréstimo
                  </h2>
                  <ul className="text-gray-500 dark:text-gray-400">
                    <li className="flex space-x-2">
                      <h2 className="font-bold">Data de início:</h2>
                      <p>{emprestimo?.dataHoraInicio}</p>
                    </li>
                    <li className="flex space-x-2">
                      <h2 className="font-bold">Data do fim:</h2>
                      <p>{emprestimo?.dataHoraFim}</p>
                    </li>
                    <li className="flex space-x-2">
                      <h2 className="font-bold">Status:</h2>
                      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                            emprestimo?.statusTransacaoItem === 'APROVADO' ? "bg-green-100 text-green-800" :
                            emprestimo?.statusTransacaoItem === 'EM_ANALISE' ? "bg-yellow-100 text-yellow-800" :
                            emprestimo?.statusTransacaoItem === 'RECUSADO' ? "bg-red-100 text-red-800" :
                            'bg-gray-500 text-white'
                          }`}>
                       {emprestimo?.statusTransacaoItem}
                      </span>
                    </li>
                    <li className="flex space-x-2">
                      <h2 className="font-bold">Observação:</h2>
                      <p>{emprestimo?.observacaoSolicitacao}</p>
                    </li>
                  </ul>
                </div>
                <div className="flex items-center space-x-4 mb-14">
                  <Button
                    type="submit"
                    className="w-full sm:w-auto bg-green-500 hover:bg-green-600 text-white font-bold py-2 px-4 rounded-lg focus:outline-none focus:shadow-outline"
                    onClick={() => handleAcao("APROVADO")}
                  >
                    Aprovar
                  </Button>
                  <Button
                    type="submit"
                    className="w-full sm:w-auto bg-red-500 hover:bg-red-600 text-white font-bold py-2 px-4 rounded-lg focus:outline-none focus:shadow-outline"
                    onClick={() => handleAcao("RECUSADO")}
                  >
                    Recusar
                  </Button>
                </div>
              </div>
            </div>
            <Separator />
            <div className="grid grid-cols-3 gap-4 mt-4 ml-5 mb-10">
              {emprestimo?.itens.map((item, id) => (
              <div key={id}>
                <h3 className="text-lg font-bold mb-2">Item</h3>
                <ul className="text-gray-500 dark:text-gray-400">
                  <li className="flex space-x-2">
                    <h2 className="font-bold">Nome do item:</h2>
                    <p>{item.nome}</p>
                  </li>

                  <li className="flex space-x-2">
                    <h2 className="font-bold">Categoria:</h2>
                    <p>{item.tipo_item}</p>
                  </li>

                  <li className="flex space-x-2">
                    <h2 className="font-bold">Quantidade para o empréstimo:</h2>
                    <p>{item.quantidade}</p>
                  </li>
                </ul>
              </div>
              ))}
              <div>
                <h3 className="text-lg font-bold mb-2">
                  Informações do usuário
                </h3>
                <ul className="text-gray-500 dark:text-gray-400">
                  <li className="flex space-x-2">
                    <h2 className="font-bold">Nome:</h2>
                    <p>{emprestimo?.solicitante.nome}</p>
                  </li>
                  <li className="flex space-x-2">
                    <h2 className="font-bold">Email:</h2>{" "}
                    <Link className="text-blue-500 hover:underline" href="#">
                      {emprestimo?.solicitante.email}
                    </Link>
                  </li>
                  <li className="flex space-x-2">
                    <h2 className="font-bold">Contato:</h2>
                    <p>{emprestimo?.solicitante.telefone}</p>
                  </li>
                </ul>
              </div>
              <div>
                <h3 className="text-lg font-bold mb-2">Local de uso</h3>
                <ul className="text-gray-500 dark:text-gray-400">
                <li className="flex space-x-2">
                    <h2 className="font-bold">CEP:</h2>
                    <p>{emprestimo?.endereco.cep}</p>
                  </li>
                <li className="flex space-x-2">
                    <h2 className="font-bold">UF:</h2>
                    <p>{emprestimo?.endereco.uf}</p>
                  </li>
                <li className="flex space-x-2">
                    <h2 className="font-bold">Cidade:</h2>
                    <p>{emprestimo?.endereco.cidade}</p>
                  </li>
                <li className="flex space-x-2">
                    <h2 className="font-bold">Bairro:</h2>
                    <p>{emprestimo?.endereco.bairro}</p>
                  </li>
                  <li className="flex space-x-2">
                    <h2 className="font-bold">Rua:</h2>
                    <p>{emprestimo?.endereco.rua}</p>
                  </li>
                  <li className="flex space-x-2">
                    <h2 className="font-bold">Número:</h2>
                    <p>{emprestimo?.endereco.numero}</p>
                  </li>
                  <li className="flex space-x-2">
                    <h2 className="font-bold">Complemento:</h2>
                    <p>{emprestimo?.endereco.complemento}</p>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </main>
    </>
  );
};

export default DetalhesEmprestimo;
