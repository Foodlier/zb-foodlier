/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable react/no-array-index-key */
import { useEffect, useState } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import { ko } from 'date-fns/esm/locale' // 한국어 설정
import 'react-datepicker/dist/react-datepicker.css'

import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import * as S from '../../styles/refrigerator/WriteRequestPage.styled'
import axiosInstance from '../../utils/FetchCall'
import { palette } from '../../constants/Styles'
import useIcon from '../../hooks/useIcon'
import ModalWithoutButton from '../../components/ui/ModalWithoutButton'
import SearchRecipeModal from '../../components/refrigerator/SearchRecipeModal'

interface RequestValue {
  title: string
  content: string
  ingredientList: string[]
  expectedPrice: number
  expectedAt: Date
  recipeId: number
}

const WriteRequestPage = () => {
  const { IcAddRound } = useIcon()
  const navigate = useNavigate()
  const { state } = useLocation()
  const requestFormId = state?.requestFormId

  const isEdit = Boolean(requestFormId)
  const [isCompleteModal, setIsCompleteModal] = useState(false)
  const [isSearchModal, setIsSearchModal] = useState(false)
  const [completeContent, setIsCompleteContent] = useState('')
  const [selectedDate, setSelectedDate] = useState<Date | null>(new Date())
  const [errorValue, setErrorValue] = useState({
    title: '',
    content: '',
    ingredientList: '',
    expectedPrice: '',
  })
  const [requestValue, setRequestValue] = useState<RequestValue>({
    title: '',
    content: '',
    ingredientList: [''],
    expectedPrice: 0,
    expectedAt: new Date(),
    recipeId: 0,
  })
  const [recipeId, setRecipeId] = useState({ title: '', id: 0 })

  // 재료 개수 추가하는 함수
  const ingredientPlus = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault()
    const updateValue = [...requestValue.ingredientList, '']
    setRequestValue({ ...requestValue, ingredientList: updateValue })
  }

  // 입력된 재료 'requestValue' 객체에 저장하는 함수
  const ingredientInputChange = (value: string, index: number) => {
    setRequestValue(prevInputs => {
      const newInputs = { ...prevInputs } // 이전 정보 가져오기
      newInputs.ingredientList[index] = value // 이전 정보에 새로운 정보 업데이트
      return newInputs
    })
  }

  const editRequest = async () => {
    try {
      const { status } = await axiosInstance.put(
        `/api/refrigerator/${requestFormId}`,
        {
          ...requestValue,
          recipeId: recipeId.id,
        }
      )
      if (status === 200) {
        setIsCompleteContent('요청서 수정이 완료되었습니다.')
      }
    } catch (error) {
      setIsCompleteContent('요청서 수정에 실패하였습니다.')
    }
    setIsCompleteModal(true)
    setTimeout(() => {
      setIsCompleteModal(false)
      navigate(-1)
    }, 1500)
  }

  const postRequest = async () => {
    try {
      const { status } = await axiosInstance.post('/api/refrigerator', {
        ...requestValue,
        recipeId: recipeId.id,
      })
      if (status === 200) {
        setIsCompleteContent('요청서 작성이 완료되었습니다.')
      }
    } catch (error) {
      console.log(error)
      setIsCompleteContent('요청서 작성에 실패하였습니다.')
    }
    setIsCompleteModal(true)
    setTimeout(() => {
      setIsCompleteModal(false)
      navigate(-1)
    }, 1500)
  }

  const checkForm = () => {
    const errors: Record<string, string> = {
      title: '',
      content: '',
      ingredientList: '',
      expectedPrice: '',
    }

    const validations = [
      {
        condition:
          requestValue.title.length < 2 || requestValue.title.length > 20,
        key: 'title',
        message: '제목을 2자 이상, 20자 이하로 입력해주세요.',
      },
      {
        condition: requestValue.content.length < 2,
        key: 'content',
        message: '내용을 2자 이상 입력해주세요.',
      },
      {
        condition: !requestValue.ingredientList.some(
          item => item.trim() !== ''
        ),
        key: 'ingredientList',
        message: '재료를 입력해주세요.',
      },
      {
        condition: requestValue.expectedPrice < 100,
        key: 'expectedPrice',
        message: '가격을 100원 이상 입력해주세요.',
      },
    ]

    validations.forEach(validation => {
      if (validation.condition) {
        errors[validation.key] = validation.message
      } else {
        errors[validation.key] = ''
      }
    })
    // 모든 오류를 한 번에 업데이트
    setErrorValue({ ...errorValue, ...errors })

    if (Object.values(errors).every(error => error === '')) {
      if (isEdit) {
        editRequest()
      } else {
        postRequest()
      }
    }
  }

  const getRequestInfo = async () => {
    const { data } = await axiosInstance.get(
      `/api/refrigerator/${requestFormId}`
    )

    setRequestValue({
      ...requestValue,
      title: data.title,
      content: data.content,
      ingredientList: data.ingredientList,
      expectedPrice: data.expectedPrice,
      expectedAt: new Date(data.expectedAt),
      recipeId: data.recipeId,
    })
    setRecipeId({ id: data.recipeId, title: data.recipeTitle })
    setSelectedDate(new Date(data.expectedAt))
  }

  useEffect(() => {
    if (isEdit) {
      getRequestInfo()
    }
  }, [])

  return (
    <>
      <Header />
      <S.RequestContainer>
        <S.RequestForm>
          <S.RequestFormList>
            <S.WrapForm>
              <S.Title>제목</S.Title>
              <S.Input
                type="text"
                placeholder="제목을 입력해주세요."
                value={requestValue.title}
                onChange={e =>
                  setRequestValue({ ...requestValue, title: e.target.value })
                }
                $width={50}
                // $marginBt={1}
              />
              <S.ErrorMessage>{errorValue.title}</S.ErrorMessage>
            </S.WrapForm>

            <S.WrapForm>
              <S.Title>요청 내용</S.Title>
              <S.Input
                type="text"
                placeholder="내용을 입력해주세요."
                value={requestValue.content}
                onChange={e =>
                  setRequestValue({ ...requestValue, content: e.target.value })
                }
                $width={50}
                // $marginBt={1}
              />
              <S.ErrorMessage>{errorValue.content}</S.ErrorMessage>
            </S.WrapForm>

            <S.WrapForm>
              <S.Title>보유 재료</S.Title>
              <S.Content>
                {requestValue.ingredientList.map((item, index) => (
                  <S.Input
                    key={`key-${index}`}
                    type="text"
                    placeholder="재료 이름"
                    value={item}
                    onChange={e => ingredientInputChange(e.target.value, index)}
                    $width={150}
                    $marginBt={1}
                  />
                ))}

                <S.AddButton onClick={ingredientPlus} $width={50}>
                  <IcAddRound size={1.2} color={palette.textSecondary} />
                  재료 추가
                </S.AddButton>
                <S.ErrorMessage>{errorValue.ingredientList}</S.ErrorMessage>
              </S.Content>
            </S.WrapForm>

            <S.WrapForm>
              <S.Title>요청가</S.Title>

              <S.Input
                type="text"
                placeholder="금액을 입력해주세요."
                value={requestValue.expectedPrice}
                onChange={e =>
                  setRequestValue({
                    ...requestValue,
                    expectedPrice: Number(e.target.value),
                  })
                }
                $width={30}
              />
              <S.ErrorMessage>{errorValue.expectedPrice}</S.ErrorMessage>
            </S.WrapForm>

            <S.WrapForm>
              <S.Title>요청시간</S.Title>
              {/* 추후 Custom 필요 */}
              <S.StyledDatePicker
                locale={ko}
                dateFormat="yyyy.MM.dd"
                shouldCloseOnSelect
                minDate={new Date()}
                selected={selectedDate}
                onChange={date => {
                  setSelectedDate(date)
                  if (date) {
                    setRequestValue({
                      ...requestValue,
                      expectedAt: date,
                    })
                  }
                }}
              />
            </S.WrapForm>

            <S.WrapForm>
              <S.Title>게시물 검색</S.Title>
              <S.FlexWrap>
                <S.Input
                  type="text"
                  value={recipeId.title}
                  readOnly
                  $width={30}
                />
                <S.SearchButton
                  type="button"
                  onClick={() => setIsSearchModal(true)}
                >
                  게시물 검색
                </S.SearchButton>
              </S.FlexWrap>
            </S.WrapForm>
          </S.RequestFormList>

          <S.ButtonList>
            <S.WritingButton type="button" onClick={() => navigate(-1)}>
              작성 취소
            </S.WritingButton>
            <S.WritingButton type="button" onClick={checkForm}>
              {isEdit ? '수정하기' : '요청서 등록'}
            </S.WritingButton>
          </S.ButtonList>
        </S.RequestForm>
      </S.RequestContainer>

      {isCompleteModal && (
        <ModalWithoutButton
          content={completeContent}
          setIsModalFalse={() => setIsCompleteModal(false)}
        />
      )}

      {isSearchModal && (
        <SearchRecipeModal
          setIsModalFalse={() => setIsSearchModal(false)}
          setRecipeId={setRecipeId}
        />
      )}

      <BottomNavigation />
    </>
  )
}

export default WriteRequestPage
