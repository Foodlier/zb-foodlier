import { useState } from 'react'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import * as S from '../../styles/refrigerator/Request.styled'

const Request = () => {
  const [count, setCount] = useState(1)
  const [inputs, setInputs] = useState<string[]>([])
  console.log(inputs)

  // 입력된 값을 배열에 저장하는 함수
  const handleInputChange = (value: string, index: number) => {
    setInputs(prevInputs => {
      const newInputs = [...prevInputs] // 이전 배열 복사
      newInputs[index] = value // 인덱스에 값 할당
      return newInputs // 새로운 배열로 상태 업데이트
    })
  }

  const countPlus = () => {
    setCount(count + 1)
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
              <S.ElementInput type="text" placeholder="제목을 입력해주세요." />
            </S.RequestFormEl>
            <S.RequestFormEl>
              <S.ElementTitle>요청 내용</S.ElementTitle>
              <S.ElementInput type="text" placeholder="내용을 입력해주세요." />
            </S.RequestFormEl>
            <S.RequestFormEl>
              <S.ElementTitle>보유 재료</S.ElementTitle>
              {Array.from({ length: count }, (value: string | '', index) => (
                <S.ElementSourceInput
                  key={index}
                  defaultValue={value}
                  type="text"
                  placeholder="재료 이름"
                  onChange={e => handleInputChange(e.target.value, index)}
                />
              ))}
              <S.PlusButton type="button" onClick={countPlus}>
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
                />
              </S.LikeInputDiv>
            </S.RequestFormEl>
            <S.RequestFormEl>
              <S.ElementTitle>요청시간</S.ElementTitle>
              <S.ElementInput type="text" placeholder="시간을 선택해주세요." />
            </S.RequestFormEl>
            <S.RequestFormEl>
              <S.ElementTitle>사용자 지역</S.ElementTitle>
              <S.LikeInputDiv>
                <S.ElementSpan>강원도 양구군 박수근로 137</S.ElementSpan>
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

export default Request
