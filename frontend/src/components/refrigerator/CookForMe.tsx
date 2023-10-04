import { useState, useEffect } from 'react'
import Modal from '../Modal'
import * as S from '../../styles/refrigerator/CookForMe.styled'
import ChefCard from '../chef/ChefCard'

interface RequestedInterface {
  chefId: number
  profileUrl: string
  nickName: string
  introduce: string
  starAvg: number
  distance: number
  lat: string
  int: string
  reviewCount: number
  recipeCount: number
  requestId: number
  isQuotation: number
}
interface ChefInterface {
  chefId: number
  profileUrl: string
  nickName: string
  introduce: string
  starAvg: number
  distance: number
  reviewCount: number
  recipeCount: number
}

const CookForMe = () => {
  const [optionToggle, setOptionToggle] = useState(false)
  const [option, setOption] = useState<string | null>('거리 순')
  const optionHandler = (e: React.MouseEvent) => {
    const target = e.target as HTMLButtonElement
    setOption(target.textContent)
    setOptionToggle(!optionToggle)
  }

  const [modalOpen, setModalOpen] = useState(false)
  const showRequest = () => {
    setModalOpen(true)
  }

  // 다른 화면 클릭 시 토글 닫힘 구현 중
  // const closeOption = () => {
  //   if (optionToggle) {
  //     setOptionToggle(false)
  //   }
  // }
  // document.addEventListener('click', closeOption)

  const OPTION_MENU_LIST = [
    '거리 순',
    '평점 순',
    '리뷰 많은 순',
    '레시피 많은 순',
  ]

  // 필터 정렬
  // const sortedChefList = CHEF_LIST_EXAMPLE//
  const [chefList, setChefList] = useState<ChefInterface[]>([])
  const [requetedList, setRequestedList] = useState<RequestedInterface[]>([])
  useEffect(() => {
    const REQUESTED_CHEF_LIST_EXAMPLE = [
      {
        chefId: 8,
        profileUrl: '',
        nickName: '나는 요리사',
        introduce: '저는 요리사 8입니다.',
        starAvg: 2.1,
        distance: 400,
        lat: '',
        int: '',
        reviewCount: 11,
        recipeCount: 4,
        requestId: 1,
        isQuotation: 1,
      },
      {
        chefId: 7,
        profileUrl: '',
        nickName: '나는 요리사',
        introduce: '저는 요리사 7입니다.',
        starAvg: 2.1,
        distance: 400,
        lat: '',
        int: '',
        reviewCount: 11,
        recipeCount: 4,
        requestId: 2,
        isQuotation: 1,
      },
    ]
    setRequestedList(REQUESTED_CHEF_LIST_EXAMPLE)
    const CHEF_LIST_EXAMPLE = [
      {
        chefId: 1,
        profileUrl: '',
        nickName: '나는 요리사',
        introduce: '저는 요리사 1입니다.',
        starAvg: 2.1,
        distance: 400,
        reviewCount: 11,
        recipeCount: 4,
      },
      {
        chefId: 2,
        profileUrl: '',
        nickName: '나는 요리사2',
        introduce: '저는 요리사 2입니다.',
        starAvg: 0.4,
        distance: 100,
        reviewCount: 12,
        recipeCount: 2,
      },
      {
        chefId: 3,
        profileUrl: '',
        nickName: '나는 요리사3',
        introduce: '저는 요리사 3입니다.',
        starAvg: 4.7,
        distance: 300,
        reviewCount: 13,
        recipeCount: 6,
      },
      {
        chefId: 4,
        profileUrl: '',
        nickName: '나는 요리사4',
        introduce: '저는 요리사 4입니다.',
        starAvg: 3.5,
        distance: 180,
        reviewCount: 14,
        recipeCount: 3,
      },
    ]
    const sortingChefList = () => {
      let sortedChefList: ChefInterface[] = []
      switch (option) {
        case '거리 순':
          sortedChefList = CHEF_LIST_EXAMPLE.sort(
            (a, b) => a.distance - b.distance
          )
          break
        case '평점 순':
          sortedChefList = CHEF_LIST_EXAMPLE.sort(
            (a, b) => b.starAvg - a.starAvg
          )
          break
        case '리뷰 많은 순':
          sortedChefList = CHEF_LIST_EXAMPLE.sort(
            (a, b) => b.reviewCount - a.reviewCount
          )
          break
        case '레시피 많은 순':
          sortedChefList = CHEF_LIST_EXAMPLE.sort(
            (a, b) => b.recipeCount - a.recipeCount
          )
          break
        default:
          break
      }
      setChefList(sortedChefList)
    }
    sortingChefList()
  }, [option])

  return (
    <>
      {modalOpen && <Modal setModalOpen={setModalOpen} modalType="request" />}
      <S.ChefListContainer>
        <S.Info>
          <S.SubTitle>내 주변 요리사</S.SubTitle>
          <S.SelectBox>
            <S.SelectedBox>
              <S.OptionButton
                onClick={() => {
                  setOptionToggle(!optionToggle)
                }}
              >
                <div>{option}</div>
                <div>▽</div>
              </S.OptionButton>
              <S.OptionList $toggle={optionToggle}>
                {OPTION_MENU_LIST.map(el => (
                  <S.Option key={el}>
                    <S.OptionButton type="button" onClick={optionHandler}>
                      {el}
                    </S.OptionButton>
                  </S.Option>
                ))}
              </S.OptionList>
            </S.SelectedBox>
          </S.SelectBox>
        </S.Info>
        <S.CardList>
          {requetedList.map(el => (
            <ChefCard key={el.chefId} el={el} isRequest="true" />
          ))}
          {chefList.map(el => (
            <ChefCard key={el.chefId} el={el} isRequest="false" />
          ))}
        </S.CardList>
        <S.ButtonList>
          <S.WritingButton type="button">+ 요청서 작성하기</S.WritingButton>
          <S.WritingButton type="button" onClick={showRequest}>
            요청서 목록
          </S.WritingButton>
        </S.ButtonList>
      </S.ChefListContainer>
    </>
  )
}

export default CookForMe
