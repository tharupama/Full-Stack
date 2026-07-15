export interface BookUpdateDto {
  id: number;
  title: string;
  imgUrl: string;
  category: string;
  author: string;
  description: string;
  price: number;
  stock: number;
  isAvailable: boolean;
}
