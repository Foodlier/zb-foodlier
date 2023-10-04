import { useState } from 'react'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import * as S from '../../styles/refrigerator/WriteRequestPage.styled'

interface RequestValue {
  title: string
  content: string
  ingredientList: string[]
  expectedPrice: string
  expectedAt: string
}

const WriteRequestPage = () => {
  const [ingredientCount, setIngredientCount] = useState(1)
  const [requestValue, setRequestValue] = useState<RequestValue>({
    title: '',
    content: '',
    ingredientList: [],
    expectedPrice: '',
    expectedAt: '',
  })

  // 재료 개수 추가하는 함수
  const ingredientPlus = () => {
    setIngredientCount(ingredientCount + 1)
  }

  // 입력된 재료 'requestValue' 객체에 저장하는 함수
  const ingredientInputChange = (value: string, index: number) => {
    setRequestValue(prevInputs => {
      const newInputs = { ...prevInputs } // 이전 정보 가져오기
      newInputs.ingredientList[index] = value // 이전 정보에 새로운 정보 업데이트
      return newInputs
    })
  }

  const BUTTON_LIST = ['작성 취소', '저장하기']
  return (
    <>
      <Header />
      <S.RequestContainer>
        <S.RequestHeader>요청서</S.RequestHeader>
        <S.RequestForm>
          <S.RequestFormList>
            <S.RequestFormEl>
              <S.ElementTitle>제목</S.ElementTitle>
              <S.ElementInput
                type="text"
                placeholder="제목을 입력해주세요."
                onChange={e =>
                  setRequestValue({ ...requestValue, title: e.target.value })
                }
              />
            </S.RequestFormEl>
            <S.RequestFormEl>
              <S.ElementTitle>요청 내용</S.ElementTitle>
              <S.ElementInput
                type="text"
                placeholder="내용을 입력해주세요."
                onChange={e =>
                  setRequestValue({ ...requestValue, content: e.target.value })
                }
              />
            </S.RequestFormEl>
            <S.RequestFormEl>
              <S.ElementTitle>보유 재료</S.ElementTitle>
              {Array.from(
                { length: ingredientCount },
                (value: string | '', index) => (
                  <S.ElementSourceInput
                    key={index}
                    defaultValue={value}
                    type="text"
                    placeholder="재료 이름"
                    onChange={e => ingredientInputChange(e.target.value, index)}
                  />
                )
              )}
              <S.PlusButton type="button" onClick={ingredientPlus}>
                +
              </S.PlusButton>
            </S.RequestFormEl>
            <S.RequestFormEl>
              <S.ElementTitle>요청가</S.ElementTitle>
              <S.LikeInputDiv>
                <S.ElementSpan>₩</S.ElementSpan>
                <S.InvisibleInput
                  type="text"
                  placeholder="금액을 입력해주세요."
                  onChange={e =>
                    setRequestValue({
                      ...requestValue,
                      expectedPrice: e.target.value,
                    })
                  }
                />
              </S.LikeInputDiv>
            </S.RequestFormEl>
            <S.RequestFormEl>
              <S.ElementTitle>요청시간</S.ElementTitle>
              <S.ElementInput
                type="text"
                placeholder="희망 시간을 입력 해주세요."
                onChange={e =>
                  setRequestValue({
                    ...requestValue,
                    expectedAt: e.target.value,
                  })
                }
              />
            </S.RequestFormEl>
            <S.RequestFormEl>
              <S.ElementTitle>사용자 지역</S.ElementTitle>
              <S.LikeInputDiv>
                <S.ElementSpan>사용자 지역 초기값</S.ElementSpan>
              </S.LikeInputDiv>
            </S.RequestFormEl>
            <S.RequestFormEl>
              <S.ElementTitle>게시물 검색</S.ElementTitle>
              <S.ElementSourceInput
                type="text"
                placeholder="기존 레시피 검색하기."
              />
              <S.SearchButton type="button">게시물 검색</S.SearchButton>
            </S.RequestFormEl>
          </S.RequestFormList>
          <S.ButtonList>
            {BUTTON_LIST.map(el => (
              <S.WritingButton key={el} type="button" className="d">
                {el}
              </S.WritingButton>
            ))}
          </S.ButtonList>
        </S.RequestForm>
      </S.RequestContainer>
      <BottomNavigation />
    </>
  )
}

export default WriteRequestPage
