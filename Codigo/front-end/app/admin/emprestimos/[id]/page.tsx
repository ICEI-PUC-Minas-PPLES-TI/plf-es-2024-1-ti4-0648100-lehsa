import { Button } from "@/components/ui/button";
import { Separator } from "@/components/ui/separator";
import Link from "next/link";

const DetalhesEmprestimo = () => {
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
                      <h2 className="font-bold">Dias restantes:</h2>
                      <p>3 dias</p>
                    </li>
                    <li className="flex space-x-2">
                      <h2 className="font-bold">Dias atrasado:</h2>
                      <p>0 dias</p>
                    </li>
                    <li className="flex space-x-2">
                      <h2 className="font-bold">Status:</h2>
                      <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-yellow-100 text-yellow-800">
                        Em andamento
                      </span>
                    </li>
                    <li className="flex space-x-2">
                      <h2 className="font-bold">Observação:</h2>
                      <p>lorem ipsum</p>
                    </li>
                  </ul>
                </div>
                <div className="flex items-center space-x-4 mb-14">
                  <Button
                    type="submit"
                    className="w-full sm:w-auto bg-green-500 hover:bg-green-600 text-white font-bold py-2 px-4 rounded-lg focus:outline-none focus:shadow-outline"
                  >
                    Aprovar
                  </Button>
                  <Button
                    type="submit"
                    className="w-full sm:w-auto bg-red-500 hover:bg-red-600 text-white font-bold py-2 px-4 rounded-lg focus:outline-none focus:shadow-outline"
                  >
                    Recusar
                  </Button>
                </div>
              </div>
            </div>
            <Separator />
            <div className="grid grid-cols-3 gap-4 mt-4 ml-5 mb-10">
              <div>
                <h3 className="text-lg font-bold mb-2">Item</h3>
                <ul className="text-gray-500 dark:text-gray-400">
                  <li className="flex space-x-2">
                    <h2 className="font-bold">Nome do item:</h2>
                    <p>Becker</p>
                  </li>

                  <li className="flex space-x-2">
                    <h2 className="font-bold">Categoria:</h2>
                    <p>Vidraria</p>
                  </li>

                  <li className="flex space-x-2">
                    <h2 className="font-bold">Quantidade:</h2>
                    <p>4</p>
                  </li>
                </ul>
              </div>
              <div>
                <h3 className="text-lg font-bold mb-2">
                  Informações do usuário
                </h3>
                <ul className="text-gray-500 dark:text-gray-400">
                  <li className="flex space-x-2">
                    <h2 className="font-bold">Nome:</h2>
                    <p>Lucas Cabral Soares</p>
                  </li>
                  <li className="flex space-x-2">
                    <h2 className="font-bold">Email:</h2>{" "}
                    <Link className="text-blue-500 hover:underline" href="#">
                      lucas@gmail.com
                    </Link>
                  </li>
                  <li className="flex space-x-2">
                    <h2 className="font-bold">Contato:</h2>
                    <p>31 98765-2345</p>
                  </li>
                </ul>
              </div>
              <div>
                <h3 className="text-lg font-bold mb-2">Local de uso</h3>
                <ul className="text-gray-500 dark:text-gray-400">
                  <li className="flex space-x-2">
                    <h2 className="font-bold">Rua:</h2>
                    <p>São Paulo</p>
                  </li>
                  <li className="flex space-x-2">
                    <h2 className="font-bold">Número:</h2>
                    <p>300</p>
                  </li>
                  <li className="flex space-x-2">
                    <h2 className="font-bold">Bairro:</h2>
                    <p>Centro</p>
                  </li>
                  <li className="flex space-x-2">
                    <h2 className="font-bold">Complemento:</h2>
                    <p>apartamento 201</p>
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
