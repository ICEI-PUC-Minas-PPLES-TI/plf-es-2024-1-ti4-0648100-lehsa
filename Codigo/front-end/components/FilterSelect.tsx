import React from "react";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";

const FilterSelect = () => {
    return (
        <div>
        <Select>
            <SelectTrigger className='rounded-full border-primary text-primary'>
                <SelectValue placeholder='Filtrar'/>
            </SelectTrigger>
            <SelectContent>
                <SelectItem value='nome'>Nome</SelectItem>
                <SelectItem value='quantidade'>Quantidade</SelectItem>
                <SelectItem value='categoria'>Categoria</SelectItem>
                <SelectItem value='outro'>Outro</SelectItem>
            </SelectContent>
        </Select>
        </div>
    )
}

export default FilterSelect