export interface Chef {
  chefId: string | number
  nickname: string
  imagePath: string
}

const chefData: Chef[] = [
  {
    chefId: 1,
    nickname: '닉네임1',
    imagePath: '/src/assets/chef.svg',
  },
  {
    chefId: 2,
    nickname: '닉네임2',
    imagePath: '/src/assets/chef.svg',
  },
  {
    chefId: 3,
    nickname: '닉네임3',
    imagePath: '/src/assets/chef.svg',
  },
]

export default chefData
