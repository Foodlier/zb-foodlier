// 가격을 받아 1000단위로 콤마(,)를 추가하며 뒤에 원을 붙여 return

const returnPriceWithComma = (price: string | number) => {
  if (typeof price === 'number') {
    return `${price
      .toString()
      .replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ',')}원`
  }
  return `${price.replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ',')}원`
}

export default returnPriceWithComma
