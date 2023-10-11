/* eslint-disable react/no-array-index-key */
import React, { useState } from 'react'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import * as S from '../../styles/refrigerator/WriteQuotationPage.styled'
import useIcon from '../../hooks/useIcon'
import ListModal from '../../components/refrigerator/ListModal'
import { palette } from '../../constants/Styles'
import {
  DIFFICULTY_LIST,
  EMPTY_INGREDIENT,
  EMPTY_ORDER,
  INGREDIENT_LIST,
} from '../../constants/Data'
import {
  RecipeIngredientDtoList,
  RecipeDetailDtoList,
} from '../../constants/Interfaces'

interface Recipe {
  title: string
  content: string
  recipeIngredientDtoList: RecipeIngredientDtoList[]
  difficulty: string
  recipeDetailDtoList: RecipeDetailDtoList[]
  expectedTime: string
}

const WriteQuotationPage = () => {
  const [modalOpen, setModalOpen] = useState(false)
  const showRequest = () => {
    setModalOpen(true)
  }

  const { IcAddRound, IcFileDockLight } = useIcon()

  const [recipeValue, setRecipeValue] = useState<Recipe>({
    title: '',
    content: '',
    recipeIngredientDtoList: [{ ...EMPTY_INGREDIENT }],
    difficulty: '',
    recipeDetailDtoList: [{ ...EMPTY_ORDER }],
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
      const updateValue = [
        ...recipeValue.recipeDetailDtoList,
        { ...EMPTY_ORDER },
      ]
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

    updatedValue[index].cookingOrder = e.target.value

    setRecipeValue(prevRecipeValue => ({
      ...prevRecipeValue,
      recipeDetailDtoList: updatedValue,
    }))
  }

  return (
    <>
      {modalOpen && (
        <ListModal setModalOpen={setModalOpen} modalType="estimate" />
      )}
      <Header />
      <S.Container>
        <S.WrapQuitation>
          <IcFileDockLight size={2} color={palette.textPrimary} />
          <S.QuotationButton onClick={showRequest}>
            견적서 목록 불러오기
          </S.QuotationButton>
        </S.WrapQuitation>

        <S.WrapForm>
          <S.Title>제목</S.Title>
          <S.Input
            placeholder="제목을 입력해주세요"
            onChange={e =>
              setRecipeValue({ ...recipeValue, title: e.target.value })
            }
          />
        </S.WrapForm>

        <S.WrapForm>
          <S.Title>설명</S.Title>
          <S.Input
            placeholder="설명을 입력해주세요"
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
              $width={30}
              $marginRi={0.6}
            />
            분 이내
          </S.WrapTime>
        </S.WrapForm>

        <S.WrapForm>
          <S.Title>예상 조리순서</S.Title>
          {recipeValue.recipeDetailDtoList.map((_, index) => (
            <S.WrapOrder key={`key]${index}`}>
              <S.Input
                onChange={e => updateOrder(e, index)}
                placeholder="조리 순서를 입력해주세요."
                $width={70}
              />
            </S.WrapOrder>
          ))}

          <S.AddButton onClick={() => addItem('order')} $width={50}>
            <IcAddRound size={1.2} color={palette.textSecondary} />
            조리 내용 추가
          </S.AddButton>
        </S.WrapForm>

        <S.RequestButton>레시피 등록하기</S.RequestButton>
      </S.Container>

      <BottomNavigation />
    </>
  )
}

export default WriteQuotationPage
