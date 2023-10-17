import { atom } from 'recoil'
import { Profile } from '../constants/Interfaces'

// My Profile
export const myInfoState = atom<Profile | null>({
  key: 'myInfoStateState',
  default: null,
})

// 냉장고를 부탁해 - 요청 보낼 요리사의 Id
export const requireChefIdState = atom<number>({
  key: 'requireChefIdState',
  default: 0,
})
