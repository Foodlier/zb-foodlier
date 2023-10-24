/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable react/no-array-index-key */
import React, { useEffect, useState } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import * as S from '../../styles/refrigerator/WriteQuotationPage.styled'
import useIcon from '../../hooks/useIcon'
import { palette } from '../../constants/Styles'
import {
  DIFFICULTY_LIST,
  EMPTY_INGREDIENT,
  INGREDIENT_LIST,
} from '../../constants/Data'
import { RecipeIngredientDtoList } from '../../constants/Interfaces'
import axiosInstance from '../../utils/FetchCall'
import ModalWithoutButton from '../../components/ui/ModalWithoutButton'

interface Recipe {
  title: string
  content: string
  recipeIngredientDtoList: RecipeIngredientDtoList[]
  difficulty: string
  recipeDetailDtoList: string[]
  expectedTime: string
}

const WriteQuotationPage = () => {
  const navigate = useNavigate()
  const { state } = useLocation()
  const quotationId = state?.quotationId

  const isEdit = Boolean(quotationId)

  const { IcAddRound } = useIcon()

  const [completeModalContent, setCompleteModalContent] = useState('')
  const [isCompleteModal, setIsCompleteModal] = useState(false)
  const [recipeValue, setRecipeValue] = useState<Recipe>({
    title: '',
    content: '',
    recipeIngredientDtoList: [{ ...EMPTY_INGREDIENT }],
    // 재료 제거
    difficulty: '',
    recipeDetailDtoList: [''],
    expectedTime: '',
  })

  // 레시피 재료 or 순서 추가
  const addItem = (key: string) => {
    if (key === 'ingredient') {
      const updateValue = [
        ...recipeValue.recipeIngredientDtoList,
        {
          ...EMPTY_INGREDIENT,
        },
      ]
      setRecipeValue({
        ...recipeValue,
        recipeIngredientDtoList: [...updateValue],
      })
    } else {
      const updateValue = [...recipeValue.recipeDetailDtoList, '']
      setRecipeValue({
        ...recipeValue,
        recipeDetailDtoList: [...updateValue],
      })
    }
  }

  // 해당 index의 재료 값 수정
  const updateIngredient = (
    e: React.ChangeEvent<HTMLInputElement>,
    index: number,
    category: string
  ) => {
    const updatedValue = [...recipeValue.recipeIngredientDtoList]
    if (category === 'count') {
      updatedValue[index][category] = Number(e.target.value)
    } else if (category === 'name' || category === 'unit') {
      updatedValue[index][category] = e.target.value
    }

    setRecipeValue(prevRecipeValue => ({
      ...prevRecipeValue,
      recipeIngredientDtoList: updatedValue,
    }))
  }

  // 해당 index의 순서 값 수정
  const updateOrder = (
    e: React.ChangeEvent<HTMLInputElement>,
    index: number
  ) => {
    const updatedValue = [...recipeValue.recipeDetailDtoList]

    updatedValue[index] = e.target.value

    setRecipeValue(prevRecipeValue => ({
      ...prevRecipeValue,
      recipeDetailDtoList: updatedValue,
    }))
  }

  const getQuotation = async () => {
    try {
      const { data, status } = await axiosInstance.get(
        `/api/quotation/${quotationId}`
      )
      if (status === 200) {
        setRecipeValue({
          ...recipeValue,
          title: data.title,
          content: data.content,
          recipeIngredientDtoList: data.recipeIngredientDtoList,
          difficulty: data.difficulty,
          recipeDetailDtoList: data.recipeDetailDtoList,
          expectedTime: data.expectedTime,
        })
      }
    } catch (error) {
      console.log(error)
    }
  }

  const editQuotation = async () => {
    try {
      const { status } = await axiosInstance.put(
        `/api/quotation/${quotationId}`,
        recipeValue
      )
      if (status === 200) {
        setCompleteModalContent('견적서 수정이 완료되었습니다.')
      }
    } catch (error) {
      setCompleteModalContent('견적서 수정에 실패하였습니다.')
    }
    setIsCompleteModal(true)
    setTimeout(() => {
      setIsCompleteModal(false)
      navigate(-1)
    }, 1500)
  }

  const postQuotation = async () => {
    try {
      const { status } = await axiosInstance.post('/api/quotation', recipeValue)
      if (status === 200) {
        setCompleteModalContent('견적서 작성이 완료되었습니다.')
      }
    } catch (error) {
      setCompleteModalContent('견적서 작성에 실패하였습니다.')
    }
    setIsCompleteModal(true)
    setTimeout(() => {
      setIsCompleteModal(false)
      navigate(-1)
      // delay props로 넘겨주어 설정 가능하도록 수정
    }, 1500)
  }

  useEffect(() => {
    if (isEdit) {
      getQuotation()
    }
  }, [])

  return (
    <>
      <Header />
      <S.Container>
        <S.WrapForm>
          <S.Title>제목</S.Title>
          <S.Input
            placeholder="제목을 입력해주세요"
            value={recipeValue.title}
            onChange={e =>
              setRecipeValue({ ...recipeValue, title: e.target.value })
            }
          />
        </S.WrapForm>

        <S.WrapForm>
          <S.Title>설명</S.Title>
          <S.Input
            placeholder="설명을 입력해주세요"
            value={recipeValue.content}
            onChange={e =>
              setRecipeValue({ ...recipeValue, content: e.target.value })
            }
          />
        </S.WrapForm>

        <S.WrapForm>
          <S.Title>예상 재료</S.Title>
          {recipeValue.recipeIngredientDtoList.map((item, index) => (
            <S.WrapIngredient key={`key-${index}`}>
              <S.WrapItemInput>
                {INGREDIENT_LIST.map(ingredientItem => (
                  <S.ItemInput
                    key={ingredientItem.value}
                    placeholder={ingredientItem.placeholder}
                    onChange={e =>
                      updateIngredient(e, index, ingredientItem.value)
                    }
                    value={item[ingredientItem.value]}
                    $width={ingredientItem.width}
                    $radius={ingredientItem.radius}
                  />
                ))}
              </S.WrapItemInput>
            </S.WrapIngredient>
          ))}
          <S.AddButton onClick={() => addItem('ingredient')} $width={50}>
            <IcAddRound size={1.2} color={palette.textSecondary} />
            재료 추가
          </S.AddButton>
        </S.WrapForm>

        <S.WrapForm>
          <S.Title>예상 난이도</S.Title>
          <S.WrapDifficultyButton>
            {DIFFICULTY_LIST.map(item => (
              <S.Difficulty
                key={item.content}
                onClick={() =>
                  setRecipeValue({ ...recipeValue, difficulty: item.value })
                }
                $isActive={recipeValue.difficulty === item.value}
              >
                {item.content}
              </S.Difficulty>
            ))}
          </S.WrapDifficultyButton>
        </S.WrapForm>

        <S.WrapForm>
          <S.Title>예상 조리시간</S.Title>
          <S.WrapTime>
            <S.Input
              onChange={e =>
                setRecipeValue({ ...recipeValue, expectedTime: e.target.value })
              }
              value={recipeValue.expectedTime}
              $width={30}
              $marginRi={0.6}
            />
            분 이내
          </S.WrapTime>
        </S.WrapForm>

        <S.WrapForm>
          <S.Title>예상 조리순서</S.Title>
          {recipeValue.recipeDetailDtoList.map((item, index) => (
            <S.WrapOrder key={`key]${index}`}>
              <S.Input
                onChange={e => updateOrder(e, index)}
                placeholder="조리 순서를 입력해주세요."
                value={item}
                $width={70}
              />
            </S.WrapOrder>
          ))}

          <S.AddButton onClick={() => addItem('order')} $width={50}>
            <IcAddRound size={1.2} color={palette.textSecondary} />
            조리 내용 추가
          </S.AddButton>
        </S.WrapForm>

        <S.RequestButton onClick={isEdit ? editQuotation : postQuotation}>
          {isEdit ? '수정하기' : '등록하기'}
        </S.RequestButton>
      </S.Container>

      {isCompleteModal && (
        <ModalWithoutButton
          content={completeModalContent}
          setIsModalFalse={() => {
            setIsCompleteModal(false)
          }}
        />
      )}

      <BottomNavigation />
    </>
  )
}

export default WriteQuotationPage
