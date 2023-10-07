export interface Chef {
  chefId: string | number
  nickname: string
  profileUrl: string
}

export const chefData: Chef[] = [
  {
    chefId: 1,
    nickname: '닉네임1',
    profileUrl: '../../../public/images/chef.svg',
  },
  {
    chefId: 2,
    nickname: '닉네임2',
    profileUrl: '../../../public/images/chef.svg',
  },
  {
    chefId: 3,
    nickname: '닉네임3',
    profileUrl: '../../../public/images/chef.svg',
  },
]

export default chefData
