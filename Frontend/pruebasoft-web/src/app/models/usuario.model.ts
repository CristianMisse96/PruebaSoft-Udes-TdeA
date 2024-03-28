import { RolEnum } from "./enums/RolEnum";

export class Usuario{

    constructor(
        public username:string,
        public nombre: string,
        public apellido: string,
        public img?: string,
        public id?: number,
        public email?: string,
        public roles?: RolEnum[],
        public password?: string,
        public enabled?:boolean,
        ) {}

}