import { atom } from 'recoil';

// export const darkModeState = atom<boolean>({
//   key: 'darkModeState',
//   default: false,
// });

// export const searchQueryState = atom<string>({
//   key: 'searchQueryState',
//   default: '',
// });

export const userState = atom({
  key: 'userState', // 고유한 키
  default: null, // 초기 상태
});