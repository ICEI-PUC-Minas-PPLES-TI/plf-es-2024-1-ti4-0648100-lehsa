import React from 'react'
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import ImageComp from './ImageComp'

interface Props {
  id: string;
  nome: string;
  tipo_item: string;
  quantidade: number;
  emprestavel: boolean;
}

const SingleItemCard = ({ id, nome, tipo_item, quantidade, emprestavel }: Props) => {
  return (
    <div>
      <Card className="rounded-lg overflow-hidden shadow-lg mx-auto my-6 hover:shadow-xl transition-all duration-200 w-80">
        <ImageComp
          src={`http://localhost:8080/item/img/${id}`}
          alt="item picture"
          width={400}
          height={60}
          className="object-fit w-400 h-60"
        />
        <CardHeader>
          <CardTitle>{nome}</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-2 items-center gap-4 text-sm">
            <div className="font-medium">{tipo_item}</div>
            <div className="col-span-2 border-t border-gray-200 dark:border-gray-800" />
            <div className="font-semibold">Quantidade</div>
            <div className="text-right">{quantidade}</div>
            <div className="font-semibold">Emprestável</div>
            <div className="text-right">{emprestavel ? "Sim" : "Não"}</div>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}

export default SingleItemCard