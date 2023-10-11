import { atom } from 'recoil'
import { Profile } from '../constants/Interfaces'

// My Profile
export const myInfoState = atom<Profile>({
  key: 'myInfoStateState',
  default: null,
})
