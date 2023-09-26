import { useState } from 'react'
import styled from 'styled-components'

const RequestContainer = styled.div`
  display: flex;
  flex-direction: column;
  padding: 0 5%;
  width: 100%;
  margin: 0 auto;
  background-color: aliceblue;
`

const RequestHeader = styled.h2`
  display: block;
  width: 100%;
  text-align: center;
  padding-bottom: 10px;
  margin-bottom: 30px;
  font-size: 36px;
  border-bottom: 1px solid #000;
`

const RequestForm = styled.form`
  padding: 0 2%;
`

const RequestFormList = styled.ul`
  display: flex;
  flex-direction: column;
  gap: 30px;
  padding-left: 0;
`

const RequestFormEl = styled.li`
  display: flex;
  align-items: center;
  @media (max-width: 768px) {
    align-items: start;
    flex-direction: column;
  }
`

const ElementTitle = styled.span`
  display: inline-block;
  font-size: 24px;
  font-weight: 600;
  width: 150px;
`

const ElementInput = styled.input`
  width: 100%;
  height: 72px;
  border-radius: 10px;
  border: 1px solid #d9d9d9;
  box-sizing: content-box;
  padding: 0 20px;
  font-size: 24px;
  &:focus {
    outline: none;
  }
`
const ElementSourceInput = styled.input`
  width: 20%;
  height: 72px;
  border-radius: 10px;
  border: 1px solid #d9d9d9;
  padding: 0 20px;
  font-size: 24px;
  margin-right: 20px;
  box-sizing: content-box;
  &:focus {
    outline: none;
  }
  @media (max-width: 768px) {
    margin-right: 0px;
    margin-bottom: 20px;
    width: 100%;
  }
`

const ElementSpan = styled.span`
  font-size: 24px;
`

const PlusButton = styled.button`
  width: 72px;
  height: 72px;
  border: 1px solid #d9d9d9;
  border-radius: 10px;
  font-size: 24px;
`

const ButtonList = styled.div`
  display: flex;
  justify-content: center;
  gap: 20px;
  width: auto;
  margin: 0 auto;
  margin-top: 50px;
  @media (min-width: 768px) {
    margin-top: 300px;
  }
`

const WritingButton = styled.button`
  width: 146px;
  height: 44px;
  line-height: 44px;
  text-align: center;
  background-color: #e45141;
  color: #ffffff;
  border-radius: 5px;
`

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
    <RequestContainer>
      <RequestHeader>요청서</RequestHeader>
      <RequestForm>
        <RequestFormList>
          <RequestFormEl>
            <ElementTitle>제목</ElementTitle>
            <ElementInput type="text" />
          </RequestFormEl>
          <RequestFormEl>
            <ElementTitle>보유 재료</ElementTitle>
            {Array.from({ length: count }, (value: string | '', index) => (
              <ElementSourceInput
                key={index}
                defaultValue={value}
                type="text"
                onChange={e => handleInputChange(e.target.value, index)}
              />
            ))}
            <PlusButton type="button" onClick={countPlus}>
              +
            </PlusButton>
          </RequestFormEl>
          <RequestFormEl>
            <ElementTitle>요청가</ElementTitle>
            <ElementInput type="text" />
            <ElementSpan>원</ElementSpan>
          </RequestFormEl>
          <RequestFormEl>
            <ElementTitle>사용자 지역</ElementTitle>
            <ElementSpan>강원도 양구군 박수근로 137</ElementSpan>
          </RequestFormEl>
          <RequestFormEl>
            <ElementTitle>요청시간</ElementTitle>
          </RequestFormEl>
          <RequestFormEl>
            <ElementTitle>게시물 검색</ElementTitle>
            <ElementSourceInput type="text" />
          </RequestFormEl>
          <RequestFormEl>
            <ElementTitle>요청 내용</ElementTitle>
            <ElementInput type="text" />
          </RequestFormEl>
        </RequestFormList>
        <ButtonList>
          {BUTTON_LIST.map(el => (
            <WritingButton key={el} type="button" className="d">
              {el}
            </WritingButton>
          ))}
        </ButtonList>
      </RequestForm>
    </RequestContainer>
  )
}

export default Request
