import React from "react";
import Image from "next/image";
import {
    Card,
    CardContent,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import items from "../app/data.js";

type Props = {
    img: string;
    nome: string;
    quantidade: number;
    categoria: string;
};

const ItensCard = () => {
    return (
        <div className="grid grid-cols-4 gap-4 pl-60">
            {items.map((item: Props) => (
                <Card
                    key="1"
                    className="rounded-lg overflow-hidden shadow-lg w-80 max-w-80 mx-auto hover:shadow-xl transition-all duration-200 mt-10 ml-10"
                >
                    <Image
                        alt="item picture"
                        className="object-cover w-full max-h-60"
                        height="320"
                        src="../images/placeholder.svg"
                        style={{
                            aspectRatio: "320/320",
                            objectFit: "cover",
                        }}
                        width="320"
                    />
                    <CardHeader>
                        <CardTitle>{item.nome}</CardTitle>
                    </CardHeader>
                    <CardContent>
                        <div className="grid grid-cols-2 items-center gap-4 text-sm">
                            <div className="font-medium">Categoria</div>
                            <div className="text-right">{item.categoria}</div>
                            <div className="col-span-2 border-t border-gray-200 dark:border-gray-800" />
                            <div className="font-medium">Quantidade</div>
                            <div className="text-right">{item.quantidade}</div>
                        </div>
                    </CardContent>
                </Card>
            ))}
        </div>
    );
};

export default ItensCard;
